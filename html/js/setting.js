var type = '';
var editor = null;
var code = null;
var currentAction = null;
loadHome();
var localMap = {
    'fillCharacter': '填充符',
    'delimiter': '分隔符',
    'escapeCharacter': '转义符',
    'keyword': '关键字',
    'scopeCharacter': '范围符',
    'simpleWords': '特殊符',
    'operator': '运算符'
}

var colorMap = {
    'fillCharacter': '填充符',
    'delimiter': '分隔符',
    'escapeCharacter': '转义符',
    'keyword': '关键字',
    'scopeCharacter': '范围符',
    'simpleWords': '特殊符',
    'operator': '运算符',
    'error': '#FFF0000'
}

function loadHome() {
    if (window.location.href.indexOf("home") == -1) {
        return;
    }
    var E = window.wangEditor
    editor = new E('#editor')
    editor.customConfig.pasteFilterStyle = false;
    editor.customConfig.zIndex = 100;
    editor.customConfig.menus = [
        'head',
        'bold',
        'italic',
        'underline'
    ];
    editor.create()
}

function loadFile() {
    var file = document.getElementById('file').files[0];
    var reader = new FileReader();
    reader.onload = function() {
        editor.txt.clear();
        editor.txt.html('<pre>' + reader.result + '</pre>');
    }
    reader.readAsText(file);
}

function replaceAll(str, replaceKey, replaceVal) {
    var reg = new RegExp(replaceKey, 'g');
    return str.replace(reg, replaceVal || '');
}
// 初始化代码
function setCode() {
    lightyear.loading('show');
    let formData = new FormData()
    var str = editor.txt.text();
    str = replaceAll(str, "&gt;", ">");
    str = replaceAll(str, "&lt;", "<");
    code = str;
    formData.append('code', str);
    var xml = new XMLHttpRequest();
    xml.open('POST', 'http://localhost:8080/api/setCode', true);
    xml.send(formData);
    xml.onreadystatechange = function() {
        if (xml.readyState == 4 && xml.status == 200) {
            lightyear.notify("代码设置成功！", 'success', 1500);
            lightyear.loading('hide');
        }
    };
    xml.onerror = function() {
        isCode = false;
        lightyear.notify("代码设置失败！", 'danger', 100);
        lightyear.loading('hide');
    }
}
//词法分析
function lexMap() {
    var xml = new XMLHttpRequest();
    xml.open('GET', 'http://localhost:8080/api/lexMap', true);
    var functionCard = document.getElementById('functionCard');
    functionCard.innerHTML = '<div class="row" id="mesRow"><div class="card"><div class="form-group">' +
        '<div class="col-xs-3"><table class="table table-bordered"><thead><tr><th></th><th>单词</th> <th>编码</th><th>位置</th><th>信息</th></tr></thead><tbody id="errorTable1"></tbody></table></div>' +
        '<div class="col-xs-3"><table class="table table-bordered"><thead><tr><th></th><th>单词</th> <th>编码</th><th>位置</th><th>信息</th></tr></thead><tbody id="errorTable2"></tbody></table></div>' +
        '<div class="col-xs-3"><table class="table table-bordered"><thead><tr><th></th><th>单词</th> <th>编码</th><th>位置</th><th>信息</th></tr></thead><tbody id="errorTable3"></tbody></table></div>' +
        '<div class="col-xs-3"><table class="table table-bordered"><thead><tr><th></th><th>单词</th> <th>编码</th><th>位置</th><th>信息</th></tr></thead><tbody id="errorTable4"></tbody></table></div>' +
        '</div></div></div>';
    functionCard.style = "";
    functionCard.className = "";
    xml.onreadystatechange = function() {
        if (xml.readyState == 4 && xml.status == 200) {
            var json = JSON.parse(xml.responseText);
            loadSuccessData(json['right']);
            loadfailedData(json['error'])
        }
    };
    xml.send();
}

function loadSuccessData(json) {
    var errorTable1 = document.getElementById('errorTable1');
    var errorTable2 = document.getElementById('errorTable2');
    var errorTable3 = document.getElementById('errorTable3');
    var errorTable4 = document.getElementById('errorTable4');
    errorTable1.innerHTML = "";
    errorTable2.innerHTML = "";
    errorTable3.innerHTML = "";
    errorTable4.innerHTML = "";
    var ind = 0;
    for (var i in json) {
        var index = ind % 4;
        var tr = document.createElement('tr');
        ind += 1;
        tr.className = 'success';
        tr.innerHTML = '<th scope="row">' + i + '</th><td>' + json[i]['word'].replace('\n', '\\n') + '</td><td>' + json[i]['type'] + '</td><td>' + json[i]['position'] + '</td><td>' + json[i]['mes'] + '</td>';
        if (index == 0) {
            errorTable1.appendChild(tr);
        } else if (index == 1) {
            errorTable2.appendChild(tr);
        } else if (index == 2) {
            errorTable3.appendChild(tr);
        } else {
            errorTable4.appendChild(tr);
        }
    }
}

function loadGrammer(key) {
    var xml = new XMLHttpRequest();
    var errorTable = document.getElementById('functionCard');
    errorTable.innerHTML = "";
    errorTable.className = 'col-lg-6';
    errorTable.style = "margin-top:30px;"
    var table = document.createElement('table');
    table.className = "table table-bordered";
    var thread = document.createElement('thead');
    thread.innerHTML = '<tr><th>HEAD</th><th>--></th><th>PRE</th></tr>';
    table.appendChild(thread);
    xml.open('GET', 'http://localhost:8080/api/grammer?key=' + key, true);
    xml.onreadystatechange = function() {
        if (xml.readyState == 4 && xml.status == 200) {
            var json = JSON.parse(xml.responseText);
            var tbody = document.createElement('tbody');
            for (var key in json) {
                var keys = json[key].split("@");
                var tr = document.createElement('tr');
                var htmlStr = '';
                for (var i in keys) {
                    if (i == 0) {
                        htmlStr += '<tr><span class="label label-danger">' + keys[i] + '</span><td>--></td><td>';
                    } else {
                        htmlStr += '<span class="label label-info" style="margin-left:10px;">' + keys[i] + '</span>';
                    }
                }
                htmlStr += '</tr>';
                tr.innerHTML = htmlStr;
                tbody.appendChild(tr);
            }
            table.appendChild(tbody);
            errorTable.appendChild(table);
        }
    };
    xml.send();
    window.open("tree.html");
}

function loadTree() {
    var xml = new XMLHttpRequest();
    xml.open('GET', 'http://localhost:8080/api/tree', true);
    xml.onreadystatechange = function() {
        if (xml.readyState == 4 && xml.status == 200) {
            var json = JSON.parse(xml.responseText);
            console.log(json);
            window.open("/tree.html");
        }
    };
    xml.send();
}

function loadfailedData(json) {}
// 语法分析
function parsing() {

}
// 语义分析
function semantic() {

}
// 中间代码生成
function tempCode() {
    let formData = new FormData()
    var str = editor.txt.text();
    formData.append('data', str);
    var xml = new XMLHttpRequest();
    xml.open('POST', 'http://localhost:8080/tempCode', true);
    xml.send(formData);
    xml.onreadystatechange = function() {
        if (xml.readyState == 4 && xml.status == 200) {
            console.log(xml.responseText);
        }
    };
}
// 目标代码生成
function targetCode() {

}



function loadRecData(json) {
    var table = document.getElementById('dataTable');
    table.innerHTML = "";
    for (var key in json) {
        var tr = document.createElement('tr');
        tr.innerHTML = '<tr><th scope="row">' + key + '</th><td>' + localMap[key] + '</td><td>' + json[key].length + '</td></tr>';
        table.appendChild(tr);
    }
}

function initialFunctionCard(type) {
    var functionCard = document.getElementById('functionCard');
    var html = '<div class="form-group"><div class="col-xs-4"><div class="left" id="grammerBody"><div class="card"><div class="card-header"><h2>语法</h2></div>' +
        '<div class="card"><table class="table"><thead><tr><th>文法符号</th><th>集合</th></tr></thead><tbody id="grammerSympolBody"></tbody></table></div>' +
        '</div></div></div><div class="col-xs-4"><div class="left"><div class="card"><div class="card-header"><h2>First集合</h2></div>' +
        '<div class="card"><table class="table"><thead><tr><th>文法符号</th><th>集合</th></tr></thead><tbody id="firstSympolBody"></tbody></table></div>' +
        '</div></div></div><div class="col-xs-4"><div class="left"><div class="card"><div class="card-header"><h2>Follow集合</h2></div><div class="card">' +
        '<table class="table"><thead><tr><th>文法符号</th><th>集合</th></tr></thead><tbody id="followSympolBody"></tbody></table></div></div></div></div></div>';
    if (type == "ll1") {
        html += '<div class="col-xs-12"><div class="left"><div class="card "><div class="card-header"><h2>Select集合</h2></div>' +
            '<div id="sectionSympolTables"></div></div></div></div>';
    }
    functionCard.innerHTML = html;

}

function initialFirstSymbols(key) {
    var firstSymbolsBody = document.getElementById('firstSympolBody');
    var xml = new XMLHttpRequest();
    xml.open('GET', 'http://localhost:8080/api/firstSympol?key=' + key, true);
    xml.onreadystatechange = function() {
        if (xml.readyState == 4 && xml.status == 200) {
            var json = JSON.parse(xml.responseText);
            for (var key in json) {
                var tr = document.createElement('tr');
                tr.innerHTML = '<th scope="row"><span class="label label-danger">' + key + '</span></th>';
                var td = document.createElement('td');
                var html = "";
                for (var i in json[key]) {
                    html += '<span class="label label-info" style="margin-left:10px;">' + json[key][i] + '</span>';
                }
                td.innerHTML = html;
                tr.appendChild(td);
                firstSymbolsBody.appendChild(tr);
            }
        }
    };
    xml.send();
}

function initialFollowSymbols(key) {
    var followSymbolsBody = document.getElementById('followSympolBody');
    var xml = new XMLHttpRequest();
    xml.open('GET', 'http://localhost:8080/api/followSympol?key=' + key, true);
    xml.onreadystatechange = function() {
        if (xml.readyState == 4 && xml.status == 200) {
            var json = JSON.parse(xml.responseText);
            for (var key in json) {
                var tr = document.createElement('tr');
                tr.innerHTML = '<th scope="row"><span class="label label-danger">' + key + '</span></th>';
                var td = document.createElement('td');
                var html = "";
                for (var i in json[key]) {
                    html += '<span class="label label-info" style="margin-left:10px;">' + json[key][i] + '</span>';
                }
                td.innerHTML = html;
                tr.appendChild(td);
                followSymbolsBody.appendChild(tr);
            }
        }
    };
    xml.send();
}


function initialSelectSymbols(key) {
    var tables = document.getElementById("sectionSympolTables");
    var xml = new XMLHttpRequest();
    xml.open('GET', 'http://localhost:8080/api/selectSympol?key=' + key, true);
    xml.onreadystatechange = function() {
        if (xml.readyState == 4 && xml.status == 200) {
            var json = JSON.parse(xml.responseText);
            for (var key in json) {
                var thead = document.createElement('thead');
                var tbody = document.createElement('tbody');
                var table = document.createElement('table');
                table.className = "table table-bordered";
                var headFile = document.createElement('span');
                headFile.className = "label label-info";
                headFile.style = "margin-left:10px;font-size:20px;";
                headFile.innerText = "#" + key + "#";
                var headHtml = "<tr>";
                var bodyHtml = "";
                bodyHtml += "<tr>";
                for (var key1 in json[key]) {
                    if (json[key][key1].length > 0) {
                        headHtml += "<th>" + key1 + "</th>";
                        bodyHtml += "<td>";
                        for (var index in json[key][key1]) {
                            bodyHtml += json[key][key1][index] + "  ";
                        }
                        bodyHtml += "</td>";
                    }
                }
                headHtml += "</tr>";
                bodyHtml += "</tr>";
                thead.innerHTML = headHtml;
                tbody.innerHTML = bodyHtml;
                tables.appendChild(headFile);
                table.appendChild(thead);
                table.appendChild(tbody);
                tables.appendChild(table);
            }
        }

    };
    xml.send();
}

function createCard(key, values) {
    var card = document.createElement('div');
    card.className = "card";
    var innerH = '<div class="card-header"><h2>' + key + '</h2></div>';
    card.innerHTML = innerH;
    var innerCard = document.createElement('div');
    innerCard.className = "card";
    var table = document.createElement('table');
    table.className = "table";
    table.innerHTML = "<thead><tr><th>前驱</th><th>后继</th></tr></thead>";
    var tbody = document.createElement('tbody');
    for (var i in values) {
        var keys = values[i].split("@");
        var tr = document.createElement('tr');
        var htmlStr = '';
        for (var i in keys) {
            if (i == 0) {
                htmlStr += '<th scope="row"><span class="label label-danger">' + keys[i] + '</span><td>';
            } else {
                htmlStr += '<span class="label label-info" style="margin-left:10px;">' + keys[i] + '</span>';
            }
        }
        htmlStr += '</tr>';
        tr.innerHTML = htmlStr;
        tbody.appendChild(tr);
    }
    table.appendChild(tbody);
    innerCard.appendChild(table);
    card.appendChild(innerCard);
    return card;
}

function initialGrammerSymbols(key) {
    var left = document.getElementById('grammerBody');
    left.innerHTML = '';
    var xml = new XMLHttpRequest();
    xml.open('GET', 'http://localhost:8080/api/grammer?key=' + key, true);
    xml.onreadystatechange = function() {
        if (xml.readyState == 4 && xml.status == 200) {
            var json = JSON.parse(xml.responseText);
            console.log(json);
            for (var key in json) {
                left.appendChild(createCard(key, json[key]));
            }
        }
    }
    xml.send();
}

function analysis() {
    if (currentAction == "ll1") {
        window.open('LL1Analysis.html');
    } else if (currentAction == "lr") {
        window.open('LRAnalysis.html');
    } else if (currentAction == "sympol") {
        window.open('sympol.html');
    }
}

function LL1Analysis() {
    currentAction = "ll1";
    initialFunctionCard(currentAction);
    initialFirstSymbols(currentAction);
    initialFollowSymbols(currentAction);
    initialGrammerSymbols(currentAction);
    initialSelectSymbols(currentAction);
}

function LRAnalysis() {
    currentAction = "lr";
    initialFunctionCard(currentAction);
    initialFirstSymbols(currentAction);
    initialGrammerSymbols(currentAction);
    initialFollowSymbols(currentAction);
}