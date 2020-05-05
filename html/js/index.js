var totalUser=document.getElementById("totalUser");
var todayClick=document.getElementById("todayClick");
var totalClick=document.getElementById("totalClick");
var totalDownload=document.getElementById("totalDownload");

function dataSet(json){
    json=JSON.parse(json)
    click_records=json["click_records"]
    download_records=json["download_records"]
    console.log(click_records)
    console.log(download_records)
    var $dashChartBarsCnt  = jQuery( '.js-chartjs-bars' )[0].getContext( '2d' ),
    $dashChartLinesCnt = jQuery( '.js-chartjs-lines' )[0].getContext( '2d' );
    var $dashChartBarsData = {
    labels: ['一', '二', '三', '四', '五', '六', '七'],
    datasets: [
        {
            label: '点击量',
            borderWidth: 1,
            borderColor: 'rgba(0,0,0,0)',
            backgroundColor: 'rgba(51,202,185,0.5)',
            hoverBackgroundColor: "rgba(51,202,185,0.7)",
            hoverBorderColor: "rgba(0,0,0,0)",
            data: [2500, 1500, 1200, 3200, 4800, 3500, 1500]
        },
        {
            label: '下载量',
            borderWidth: 1,
            borderColor: 'rgba(0,0,0,0)',
            backgroundColor: 'rgba(51,43,185,0.5)',
            hoverBackgroundColor: "rgba(51,56,185,0.7)",
            hoverBorderColor: "rgba(0,0,0,0)",
            data: [1500, 2500, 3200, 4200, 2800, 4500, 3500]
        }
    ]
    };
    var $dashChartLinesData = {
    labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
    datasets: [
        {
            label: '点击量',
            data: [20, 25, 40, 30, 45, 40, 55, 40, 48, 40, 42, 50],
            borderColor: '#358ed7',
            backgroundColor: 'rgba(53, 142, 215, 0.175)',
            borderWidth: 1,
            fill: false,
            lineTension: 0.5
        },
        {
            label: '下载量',
            data: [20, 34, 56, 30, 35, 40, 55, 46, 48, 40, 42, 50],
            borderColor: '#358ed7',
            backgroundColor: 'rgba(53, 142, 215, 0.175)',
            borderWidth: 1,
            fill: false,
            lineTension: 0.5
        }
        
    ]
    };

    new Chart($dashChartBarsCnt, {
    type: 'bar',
    data: $dashChartBarsData
    });

    var myLineChart = new Chart($dashChartLinesCnt, {
    type: 'line',
    data: $dashChartLinesData,
    });
}
$(document).ready(function(e) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET","http://localhost:516/admin/index",true);
    xhr.onreadystatechange=function(){
        if(xhr.readyState===4&&xhr.status===200){
            dataSet(xhr.responseText);
        }
    }
    xhr.send(null);
});
