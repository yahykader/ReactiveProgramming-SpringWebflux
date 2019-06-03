
// Randomly add a data point every 500ms

/*setInterval(function() {
    s1.append(new Date().getTime(), Math.random() * 10000);
}, 500);*/
let courbes = [];
let chart = new SmoothieChart();
var colors = [
    { strokeStyle: 'rgba(0, 255, 0, 1)', fillStyle: 'rgba(0, 255, 0, 0.2)', lineWidth: 2 },
    { strokeStyle: 'rgba(255, 0, 0, 1)', fillStyle: 'rgba(255, 0, 0, 0.2)', lineWidth: 2 },
    { strokeStyle: 'rgba(0, 0, 255, 1)', fillStyle: 'rgba(0, 0, 255, 0.2)', lineWidth: 2 },
    { strokeStyle: 'rgba(200, 120, 150, 1)', fillStyle: 'rgba(100, 100, 100, 0.2)', lineWidth: 2 },
    { strokeStyle: 'rgba(200, 255, 255, 1)', fillStyle: 'rgba(200, 255, 255, 0.2)', lineWidth: 2 },
    { strokeStyle: 'rgba(255, 255, 0, 1)', fillStyle: 'rgba(255, 255, 0, 0.2)', lineWidth: 2 },

];
let index = -1;
function  onConnect(btn,id) {
    //alert(id);
     if(!courbes[id]){

         courbes[id]= new TimeSeries({scrollBackwards:true,tooltip:true});
         let color=colorRandom();
         chart.addTimeSeries(courbes[id], color);
         chart.streamTo(document.getElementById("chart"), 500);

         var connection =new EventSource("/stream/"+id);
         console.log(connection);

         connection.onmessage=function (resp){
             var transaction=JSON.parse(resp.data);
             courbes[id].append(new Date().getTime(),transaction.price);
             console.log(transaction.price);
     }
    };
     btn.style.background="#FF0000";

}
function  colorRandom() {
    ++index;
    if(index>=colors.length) index=0;
    return colors[index];

}