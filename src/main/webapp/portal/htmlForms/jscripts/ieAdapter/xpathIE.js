function ieXpathEvaluate(el, xpath) {
    var index = [];
    index[0] = xpath.indexOf('instance(');
    index[1] = xpath.indexOf('[position()');
    index[2] = xpath.indexOf('row[index(');

    var min = null;
    var choice = -1;
    for(var i=0; i<index.length; i++) {
        if(index[i] != -1 && (min === null || min > index[i])) {
            min = index[i];
            choice = i;
        }
    }

    switch (choice) {
        case 0:
            var regexp = /('[^']*instance\([^']*')|("[^"]*instance\([^"]*")|(instance\([^)]*\))/g;
            var next = xpath.replace(regexp, '');
            var pre = xpath.replace(next, '');
            pre = pre.replace(
                regexp, function(str) {
                    if (str.charAt(0) == '"' || str.charAt(0) == '\'')
                        return str;
                    str = str.replace(/^instance\(/, "/XFDL/globalpage/global/xformsmodels/xforms:model/xforms:instance[@id=");
                    str = str.replace(/\)$/, "]");
                    return str;
                });
            var newEl = el.selectNodes(pre)[0].firstChild;
            var newXpath = "." + next;
            return ieXpathEvaluate(newEl, newXpath);
        case 1:
            var key = /\[position\(\)='\d+']\//g.exec(xpath)[0];
            var next = xpath.substr(xpath.indexOf(key) + key.length, xpath.length);
            var pre = xpath.replace(/\[position\(\).+$/g, '');
            var num = parseInt(key.replace(/[^0-9]/g, ''))-1;
            var newEl = el.selectNodes(pre)[num];
            return ieXpathEvaluate(newEl, next);
        case 2:
            var tmpStr = xpath;
            var regexp = /index\('(\w*)'\)/g;
            var res = regexp.exec(tmpStr);
            while(res){
                var p = res[1];
                res = regexp.exec(tmpStr);
                for (var val in treeInfoEls._massEls){
                    if (val.indexOf(p)!=-1){
                        if (treeInfoEls._massEls[val].typeEl == "table"){
                            var index = treeInfoEls._massEls[val].selectedRow;
                            tmpStr = tmpStr.replace(regexp, index);
                        }
                    }
                }
            }
            return ieXpathEvaluate(newEl, tmpStr)
        default:
            return el.selectNodes(xpath);
    }
}