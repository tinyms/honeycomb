package com.tinyms.web;

import com.tinyms.point.IServerStartup;
import com.tinyms.point.IUploader;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public class ClassLoaderUtil {
    private final static Logger Log = Logger.getAnonymousLogger();

    /**
     * Api pool
     */
    private final static Map<String, ApiTarget> apiObjectPool = new HashMap<String, ApiTarget>();
    private final static Map<String, FunctionTarget> apiFunctionPool = new HashMap<String, FunctionTarget>();
    /**
     * Route pool
     */
    private final static Map<String, RouteTarget> routeObjectPool = new HashMap<String, RouteTarget>();
    /**
     * Plugins pool
     */
    private final static Map<String, List<Object>> plugins = new HashMap<String, List<Object>>();

    public static ApiTarget getApiObject(String key) {
        return apiObjectPool.get(key);
    }

    public static FunctionTarget getApiFunction(String key) {
        return apiFunctionPool.get(key);
    }

    public static RouteTarget getRouteObject(String path) {
        for(String k : routeObjectPool.keySet()){
            if(path.startsWith(k)){
                return routeObjectPool.get(k);
            }
        }
        return null;
    }

    public static <T> List<T> getPlugin(Class<T> cls) {
        List<T> items = new ArrayList<T>();
        List<Object> plugins_ = plugins.get(cls.getName());
        if (plugins_ == null) {
            return items;
        }
        for (Object o : plugins_) {
            items.add((T) o);
        }
        return items;
    }

    private static void putPlugin(Class<?> interface_, Class<?> o) {
        List<Object> plugins_ = plugins.get(interface_.getName());
        if (plugins_ == null) {
            plugins.put(interface_.getName(), new ArrayList<Object>());
        }
        try {
            plugins.get(interface_.getName()).add(o.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void parseApiClasses(Class cls){
        try {
            Api api = (Api) cls.getAnnotation(Api.class);
            String key = api.name();

            ApiTarget apiTarget = new ApiTarget();
            apiTarget.setAuth(api.auth());
            apiTarget.setInstance(cls.newInstance());
            apiObjectPool.put(key, apiTarget);

            Method[] methods = cls.getDeclaredMethods();
            if(methods != null){
                for(Method method : methods){
                    if(method.getModifiers()==Modifier.PUBLIC && method.isAnnotationPresent(Function.class)){
                        Function func = method.getAnnotation(Function.class);
                        String name = func.name();
                        boolean auth = func.auth();
                        if(StringUtils.isBlank(name)){
                            name = method.getName();
                        }
                        FunctionTarget functionTarget = new FunctionTarget();
                        functionTarget.setAuth(auth);
                        functionTarget.setMethod(method);
                        apiFunctionPool.put(String.format("%s.%s", key, name), functionTarget);
                    }
                }
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void parseWebModule(Class cls){
        WebModule view = (WebModule) cls.getAnnotation(WebModule.class);
        String moduleName = view.name();
        String moduleUrl = "";
        if (StringUtils.isNotBlank(moduleName)) {
            if(!moduleName.startsWith("/")){
                moduleUrl = String.format("/%s", moduleName);
            }
        }
        Method[] methods = cls.getDeclaredMethods();
        if (methods != null) {
            for (Method m : methods) {
                if (m.getModifiers() == Modifier.PUBLIC && m.isAnnotationPresent(Route.class)) {
                    Route route = m.getAnnotation(Route.class);
                    String name = route.name();
                    String mappingUrl = "";
                    if (StringUtils.isNotBlank(name)) {
                        if(!name.startsWith("/")){
                            mappingUrl = String.format("/%s", name);
                        }
                    } else {
                        mappingUrl = String.format("/%s", m.getName());
                    }

                    RouteTarget target = new RouteTarget();
                    target.setAuth(route.auth());
                    target.setParamExtractor(route.paramExtractor());
                    target.setParamPatterns(route.paramPatterns());
                    try {
                        target.setTarget(cls.newInstance());
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    target.setMethod(m);
                    routeObjectPool.put(String.format("%s%s", moduleUrl, mappingUrl), target);
                }
            }
        }
    }

    public static void loadPlugins(String packageName) {
        if ("com.tinyms".equals(packageName)) {
            apiObjectPool.clear();
            routeObjectPool.clear();
        }
        Set<Class<?>> classes = getClasses(packageName, true);
        for (Class cls : classes) {
            if (cls.isAnnotationPresent(Api.class)) {
                parseApiClasses(cls);
            } else if (cls.isAnnotationPresent(WebModule.class)) {
                parseWebModule(cls);
            } else {
                Class<?>[] interfaces = cls.getInterfaces();
                for (Class intertf : interfaces) {
                    if (intertf.getName().equals(IServerStartup.class.getName())) {
                        putPlugin(IServerStartup.class, cls);
                    } else if (intertf.getName().equals(IUploader.class.getName())) {
                        putPlugin(IUploader.class, cls);
                    }
                }
            }
        }
    }

    public static ClassLoader getStandardClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static ClassLoader getFallbackClassLoader() {
        return ClassLoaderUtil.class.getClassLoader();
    }

    public static Class load(String className) throws ClassNotFoundException {
        Class clazz;
        try {
            clazz = Class.forName(className, true, getStandardClassLoader());
        } catch (ClassNotFoundException e) {
            //try fallback
            clazz = Class.forName(className, true, getFallbackClassLoader());
        }

        return clazz;
    }

    /**
     * 从包package中获取所有的Class
     *
     * @param pack      包前缀
     * @param recursive 是否递归
     * @return 返回类的集合
     */
    public static Set<Class<?>> getClasses(String pack, boolean recursive) {

        // 第一个class类的集合
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        // 是否循环迭代
        // 获取包的名字 并进行替换
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(
                    packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    Log.info("Search class from file...");
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath,
                            recursive, classes);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
                    Log.info("Search class from jar...");
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection())
                                .getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx)
                                            .replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class")
                                            && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(
                                                packageName.length() + 1, name
                                                .length() - 6);
                                        try {
                                            // 添加到classes
                                            classes.add(Class
                                                    .forName(packageName + '.'
                                                            + className));
                                        } catch (ClassNotFoundException e) {
                                            // log
                                            // .error("添加用户自定义视图类错误 找不到此类的.class文件");
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        // log.error("在扫描用户定义视图时从jar包获取文件出错");
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName 包名
     * @param packagePath 包路径
     * @param recursive   是否递归
     * @param classes     class集合
     */
    public static void findAndAddClassesInPackageByFile(String packageName,
                                                        String packagePath, final boolean recursive, Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            // log.warn("用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory())
                        || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "."
                        + file.getName(), file.getAbsolutePath(), recursive,
                        classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    //classes.add(Class.forName(packageName + '.' + className));
                    //经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    // log.error("添加用户自定义视图类错误 找不到此类的.class文件");
                    e.printStackTrace();
                }
            }
        }
    }
}