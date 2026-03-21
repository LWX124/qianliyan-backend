//function init() {
//    var map = new qq.maps.Map(document.getElementById("container"), {
//        // 地图的中心地理坐标。
//        center: new qq.maps.LatLng(30.659178,104.067993),
//        zoom:11
//    });
//    var marker = new qq.maps.Marker({
//        position: center,
//        map: map,
//    });
//    var anchor = new qq.maps.Point(0, 39),
//        size = new qq.maps.Size(42, 68),
//        origin = new qq.maps.Point(0, 0),
//        markerIcon = new qq.maps.MarkerImage(
//            "https://3gimg.qq.com/lightmap/api_v2/2/4/99/theme/default/imgs/marker.png",
//            size,
//            origin,
//            anchor
//        );
//    marker.setIcon(markerIcon);
//
//}
//init();
var lables=[];
var markerIcons=[];

var data = [{
    "lat": 30.74670439486628,
    "lng": 104.12691593170166,
    "title": 'Nick'
  },
  {
    "lat": 30.687673549015578,
    "lng": 104.1018533706665,
    "title": 'Atom'
  },
  {
    "lat": 30.705976975704473,
    "lng": 104.1018533706665,
    "title": 'Vscode'
  },
  {
    "lat": 30.789184218246376,
    "lng": 104.1018533706665,
    "title": 'Adidas'
  },
  {
    "lat": 30.75422822971449,
    "lng": 104.10610198969997,
    "title": 'Mock'
  }
]
var center = new qq.maps.LatLng(30.656779, 104.065676);
var map = new qq.maps.Map(document.getElementById('container'), {
            center: center,
            zoom: 13
          });
          function setMark(map, position, url, width, height, title) {
            var marker = new qq.maps.Marker({
              position: position,
              map: map,
            });
            var offset = new qq.maps.Size(width / 4, height / 2),
              style = {
                color: "red",
                backgroundColor: "none",
                border: 'none'
              }
            var label = new qq.maps.Label({
              position: position,
              map: map,
              content: title,
              offset,
              style
            });
            lables.push(label);
            var anchor = new qq.maps.Point(-width / 4, height / 2),
              size = new qq.maps.Size(width, height),
              origin = new qq.maps.Point(0, 0),
              markerIcon = new qq.maps.MarkerImage(
                url,
                size,
                origin,
                anchor,
                size
              );
            marker.setIcon(markerIcon);
            markerIcons.push(marker);
            label.setMap(map)
          }
var init = function () {
    var ajax = new $ax(Feng.ctxPath + "/tx/map/adjustersLocation", function (result) {
    if(result){
        data=result;
          data.forEach(item => {
            var position = new qq.maps.LatLng(item.lat, item.lng);
            setMark(map, position, '/static/img/car.png', 60, 18, item.title)
          })
         }
    }, function (result) {
    });
     ajax.start();
}
//删除覆盖物
function clearOverlays() {
    var overlay;
    while (overlay = lables.pop()) {
           overlay.setMap(null);
           markerIcons.pop().setMap(null);
    }
    init();
}
$('body').everyTime('10s',clearOverlays);
init();