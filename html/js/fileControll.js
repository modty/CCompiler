var files={};
var voices={};
var images={};
var tp;
function F_Open_dialog(tp) 
{ 
    this.tp=tp;
    if(tp=='file'){
        document.getElementById("btn_file").click(); 
    }else if(tp=='voice'){
        document.getElementById("btn_voice").click(); 
    }else if(tp=='image'){
        document.getElementById("btn_image").click(); 
    }
}

function reads(obj) {
    var div;
    var file = obj.files[0];
    var reader = new  FileReader();
    reader.readAsDataURL(file);
    reader.onload = function (ev) {
        var figure=document.createElement('figure');
        var li=document.createElement('li');
        var img=document.createElement('img');
        var a=document.createElement('a');
        a.innerText=file.name;
        var figcaption=document.createElement('figcaption');
        var a1=document.createElement('a');
        var i=document.createElement('i');
        i.className="mdi mdi-delete";
        a1.className="btn btn-round btn-square btn-danger";
        a1.appendChild(i);
        figcaption.appendChild(a1);
        if(tp=='file'){
            div=document.getElementById('file-list')
            img.src="images/gallery/file.png";
            files[file.name]=file;
            a1.onclick=(function(){
                delete files[file.name];
                li.parentNode.removeChild(li);
            });
        }else if(tp=='voice'){
            voices[file.name]=file;
            div=document.getElementById('voice-list')
            img.src="images/gallery/voice.png";
            a1.onclick=(function(){
                delete voices[file.name];
                li.parentNode.removeChild(li);
            });
        }else if(tp=='image'){
            images[file.name]=file;
            div=document.getElementById('image-list')
            img.src=ev.target.result;
            a1.onclick=(function(){
                delete images[file.name];
                li.parentNode.removeChild(li);
            });
            var a2=document.createElement('a');
            var i2=document.createElement('i');
            i2.className="mdi mdi-eye";
            a2.className="btn btn-round btn-square btn-primary";
            a2.appendChild(i2);
            figcaption.appendChild(a2);
        }
        
        img.onload=(function(){
            img.width=160*img.width/img.height;
            img.height=160;
            figure.appendChild(img);
            figure.appendChild(a);
            figure.appendChild(figcaption);
            var last=div.lastElementChild;
            div.removeChild(last);
            li.appendChild(figure);
            div.appendChild(li);
            div.appendChild(last);
        });
    }
 }
 function uploadArticle(){
    var formData1 = new FormData();
    var formData2 = new FormData();
    for(var k in voices){
        formData1.append('voices',voices[k]);
    }
    for(var i in files){
        formData2.append('articles', files[i]); // 文件数据
    }
    for(var j in images){
        formData1.append('images',images[j]);
    }
    var xhr2=new XMLHttpRequest();
    xhr2.onreadystatechange=function(){
        if (xhr2.readyState === 4 && xhr2.status === 200&&xhr1.readyState === 4 && xhr1.status === 200) {
            alert("上传成功");
            window.location.reload();
        }
    }
    var xhr1 = new XMLHttpRequest();
    xhr1.onreadystatechange = function() {
        if (xhr2.readyState === 4 && xhr2.status === 200&&xhr1.readyState === 4 && xhr1.status === 200) {
            alert("上传成功");
            window.location.reload();
        }
    };
    xhr1.open("POST","/upload/uploadRes")
    xhr2.open("POST","/lol/uploadArticle")
    xhr1.send(formData1);
    xhr2.send(formData2);
}