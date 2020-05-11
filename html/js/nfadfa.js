function getExpress() {
    var express = '{D}\\.{D}*|{D}*\\.{D}';
    var text = document.getElementById('example-nf-email').value;
    if (text != '') {
        express = text;
    }
    return express;
}

function getDefine() {
    var defin = document.getElementById('example-tags_tagsinput');
    var child = defin.childNodes;
    var defineMap = {};
    for (var i = 0; i < child.length - 2; i++) {
        var defineText = child[i].firstChild.textContent.trim().split("==");
        defineMap[defineText[0]] = defineText[1];
    }
    return defineMap;
}

function createNFA() {
    var defineMap = getDefine();
    let formData = new FormData()
    for (let i in defineMap) {
        formData.append(i, defineMap[i]);
    }
    var xml = new XMLHttpRequest();
    xml.open('POST', 'http://localhost:8080/api/nfaDfaMacro', true);
    xml.onreadystatechange = function() {
        if (xml.readyState == 4 && xml.status == 200) {
            var express = getExpress();
            formData.append('expr', express);
            var xml1 = new XMLHttpRequest();
            xml1.open('POST', 'http://localhost:8080/api/nfaDfaExpr', true);
            xml1.onreadystatechange = function() {
                if (xml1.readyState == 4 && xml1.status == 200) {
                    var json = JSON.parse(xml1.responseText);
                    var state = json['state'];
                    var edg = json['edg'];
                    var statePoint = 1; // 当前选中的点
                    diagGraph.init(statePoint, state, edg, true, 'svgCanvasNfa'); //创建关系图
                    var svgCanvas = document.getElementById('svgCanvasNfa'); //绑定事件鼠标点击
                }
            }
            xml1.send(formData);
        }
    }
    xml.send(formData);
}

function nfaDfaToDfa() {
    var defineMap = getDefine();
    let formData = new FormData()
    for (let i in defineMap) {
        formData.append(i, defineMap[i]);
    }
    var xml = new XMLHttpRequest();
    xml.open('POST', 'http://localhost:8080/api/nfaDfaMacro', true);
    xml.onreadystatechange = function() {
        if (xml.readyState == 4 && xml.status == 200) {
            var express = getExpress();
            formData.append('expr', express);
            var xml1 = new XMLHttpRequest();
            xml1.open('POST', 'http://localhost:8080/api/nfaDfaToDfa', true);
            xml1.onreadystatechange = function() {
                if (xml1.readyState == 4 && xml1.status == 200) {
                    var json = JSON.parse(xml1.responseText);
                    var state = json['state'];
                    var edg = json['edg'];
                    var statePoint = 0; // 当前选中的点
                    diagGraph.init(statePoint, state, edg, true, 'svgCanvasDfa'); //创建关系图
                }
            }
            xml1.send(formData);
        }
    }
    xml.send(formData);
}

function nfaDfaMinDfa() {
    var defineMap = getDefine();
    let formData = new FormData()
    for (let i in defineMap) {
        formData.append(i, defineMap[i]);
    }
    var xml = new XMLHttpRequest();
    xml.open('POST', 'http://localhost:8080/api/nfaDfaMacro', true);
    xml.onreadystatechange = function() {
        if (xml.readyState == 4 && xml.status == 200) {
            var express = getExpress();
            formData.append('expr', express);
            var xml1 = new XMLHttpRequest();
            xml1.open('POST', 'http://localhost:8080/api/nfaDfaMinDfa', true);
            xml1.onreadystatechange = function() {
                if (xml1.readyState == 4 && xml1.status == 200) {
                    var json = JSON.parse(xml1.responseText);
                    var state = json['state'];
                    var edg = json['edg'];
                    console.log(state);
                    console.log(edg);
                    var statePoint = 0; // 当前选中的点
                    diagGraph.init(statePoint, state, edg, true, 'svgCanvasMinDfa'); //创建关系图
                }
            }
            xml1.send(formData);
        }
    }
    xml.send(formData);
}