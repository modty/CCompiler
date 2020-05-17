var type = '';
var editor = null;
var code = null;
loadHome();

function loadFile() {
    var file = document.getElementById('file').files[0];
    var reader = new FileReader();
    reader.onload = function() {
        editor.txt.clear();
        editor.txt.html('<pre>' + reader.result + '</pre>');
    }
    reader.readAsText(file);
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

function setCode() {
    let formData = new FormData()
    var str = editor.txt.text();
    str = replaceAll(str, "&gt;", ">");
    str = replaceAll(str, "&lt;", "<");
    code = str;
    formData.append('code', str);
    var xml = new XMLHttpRequest();
    xml.open('POST', 'http://localhost:8080/api/setSymbolGrammer', true);
    xml.send(formData);
    xml.onreadystatechange = function() {
        if (xml.readyState == 4 && xml.status == 200) {
            lightyear.notify("代码设置成功！", 'success', 1500);
            lightyear.loading('hide');
            initialFirstVT();
            initialLastVT();
            initialSymbolTable();
        }
    };
    xml.onerror = function() {
        isCode = false;
        lightyear.notify("代码设置失败！", 'danger', 100);
        lightyear.loading('hide');
    }
}

function initialFirstVT() {
    var firstVTTable = document.getElementById('firstVTTable');
    var xml1 = new XMLHttpRequest();
    xml1.open('GET', 'http://localhost:8080/api/firstSympol?key=symbol', true);
    xml1.onreadystatechange = function() {
        if (xml1.readyState == 4 && xml1.status == 200) {
            var json = JSON.parse(xml1.responseText);
            console.log(json);
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
                firstVTTable.appendChild(tr);
            }
        }
    }
    xml1.send();
}

function initialLastVT() {
    var lastVTTable = document.getElementById('lastVTTable');
    var xml1 = new XMLHttpRequest();
    xml1.open('GET', 'http://localhost:8080/api/followSympol?key=symbol', true);
    xml1.onreadystatechange = function() {
        if (xml1.readyState == 4 && xml1.status == 200) {
            var json = JSON.parse(xml1.responseText);
            console.log(json);
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
                lastVTTable.appendChild(tr);
            }
        }
    }
    xml1.send();
}

function initialSymbolTable() {
    var symbolTable = document.getElementById('symbolTable');
    var xml1 = new XMLHttpRequest();
    xml1.open('GET', 'http://localhost:8080/api/selectSympol?key=symbol', true);
    xml1.onreadystatechange = function() {
        if (xml1.readyState == 4 && xml1.status == 200) {
            var json = JSON.parse(xml1.responseText);
            console.log(json);
            var table = document.createElement('table');
            var thead = document.createElement('thead');
            var tbody = document.createElement('tbody');
            var bodyHtml = "";
            table.className = "table table-bordered";
            for (var key in json) {
                var headHtml = "<tr><th></th>";
                bodyHtml += "<tr><th scope='row'>" + key + "</th>";
                for (var key1 in json[key]) {
                    if (json[key][key1].length > 0) {
                        console.log(json[key][key1]);
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
                table.appendChild(thead);
                table.appendChild(tbody);
                symbolTable.appendChild(table);
            }
        }
    }
    xml1.send();
}

function analysis() {
    var express = getExpress();
    var xml1 = new XMLHttpRequest();
    var actionTable = document.getElementById('actionTable');
    let formData = new FormData()
    formData.append('code', express);
    xml1.open('POST', 'http://localhost:8080/api/symbolAnalysis', true);
    xml1.onreadystatechange = function() {
        if (xml1.readyState == 4 && xml1.status == 200) {
            var json = JSON.parse(xml1.responseText);
            var table = document.createElement('table');
            var thead = document.createElement('thead');
            var tbody = document.createElement('tbody');
            var bodyHtml = "";
            table.className = "table table-bordered";
            for (var key in json) {
                var headHtml = "<tr><th></th>";
                bodyHtml += "<tr><th scope='row'>" + key + "</th>";
                for (var key1 in json[key]) {
                    if (json[key][key1].length > 0) {
                        console.log(json[key][key1]);
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
                table.appendChild(thead);
                table.appendChild(tbody);
                actionTable.appendChild(table);
            }
        }
    }
    xml1.send(formData);
}

function getExpress() {
    var express = 'a + a';
    var text = document.getElementById('example-nf-email').value;
    if (text != '') {
        express = text;
    }
    return express;
}

function replaceAll(str, replaceKey, replaceVal) {
    var reg = new RegExp(replaceKey, 'g');
    return str.replace(reg, replaceVal || '');
}