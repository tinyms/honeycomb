/*
YUI 3.14.1 (build 63049cb)
Copyright 2013 Yahoo! Inc. All rights reserved.
Licensed under the BSD License.
http://yuilibrary.com/license/
*/

YUI.add("paginator-url",function(e,t){function n(){}n.ATTRS={pageUrl:{}},n.prototype={prevPageUrl:function(){return this.hasPrevPage()&&this.formatPageUrl(this.get("page")-1)||null},nextPageUrl:function(){return this.hasNextPage()&&this.formatPageUrl(this.get("page")+1)||null},formatPageUrl:function(t){var n=this.get("pageUrl");return n?e.Lang.sub(n,{page:t||this.get("page")}):null}},e.namespace("Paginator").Url=n,e.Base.mix(e.Paginator,[n])},"3.14.1",{requires:["paginator"]});
