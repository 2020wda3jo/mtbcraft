$(document).ready(function() {
		$('#courseInfo').hide();
		$('#box_post_DA').hide();
		
});

var userId = $("#hiddenID").val();

var da_makers = []; // 위험지역을 담을 배열

var mapContainer = document.getElementById('mymap'), // 지도를 표시할 div 
mapOption = { 
    center: new kakao.maps.LatLng(35.89617906303501, 128.62171907592318), // 지도의 중심좌표
    level: 5 // 지도의 확대 레벨
};
var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

//지도를 클릭한 위치에 표출할 마커입니다
var da_Point = new kakao.maps.Marker({ 
    // 지도 중심좌표에 마커를 생성합니다 
    position: map.getCenter() 
}); 

// 주소-좌표 변환 객체를 생성합니다
var geocoder = new kakao.maps.services.Geocoder();

// 지도에 코스를 나타내기위한 선입니다
var polyline = new kakao.maps.Polyline({
    path:  [
        new kakao.maps.LatLng(33.452344169439975, 126.56878163224233),
        new kakao.maps.LatLng(33.452739313807456, 126.5709308145358),
        new kakao.maps.LatLng(33.45178067090639, 126.5726886938753) 
    ], // 선을 구성하는 좌표배열 입니다
    strokeWeight: 7, // 선의 두께 입니다
    strokeColor: "#FF6600", // 선의 색깔입니다
    strokeOpacity: 0.5, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
    strokeStyle: 'solid' // 선의 스타일입니다
});

// 선택한 코스를 나타내기 위한 선입니다
var selectedPolyline = new kakao.maps.Polyline({
    path: [
        new kakao.maps.LatLng(33.452344169439975, 126.56878163224233),
        new kakao.maps.LatLng(33.452739313807456, 126.5709308145358),
        new kakao.maps.LatLng(33.45178067090639, 126.5726886938753) 
    ], // 선을 구성하는 좌표배열 입니다
    strokeWeight: 15, // 선의 두께 입니다
    strokeColor: "#00FF00", // 선의 색깔입니다
    strokeOpacity: 0.2, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
    strokeStyle: 'solid' // 선의 스타일입니다
});

//위험지역 표시 체크 클릭시
$("#chk_DA").click(function(){
	if( $("#chk_DA").is(":checked") ){
		getDA();
	}else{
		hideMarkers();
	}
});


//위험지역 조회
function getDA(){
	da_makers = [];
	$.ajax({
		url : "/riding/DA",
		type : "GET",
		cache : false,
		success : function(data) {
			for (var i = 0; i < data.length; i ++) {
			    // 마커를 생성합니다
			    var marker = new kakao.maps.Marker({
			        map: map, // 마커를 표시할 지도
			        position: new kakao.maps.LatLng(data[i].da_latitude, data[i].da_longitude) // 마커의 위치
			    });
			    
			    da_makers.push(marker);

			    // 마커에 표시할 인포윈도우를 생성합니다 
			    var infowindow = new kakao.maps.InfoWindow({
			        content: data[i].da_content // 인포윈도우에 표시할 내용
			    });

			    // 마커에 mouseover 이벤트와 mouseout 이벤트를 등록합니다
			    // 이벤트 리스너로는 클로저를 만들어 등록합니다 
			    // for문에서 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트가 등록됩니다
			    kakao.maps.event.addListener(marker, 'mouseover', makeOverListener(map, marker, infowindow));
			    kakao.maps.event.addListener(marker, 'mouseout', makeOutListener(infowindow));
			}
		}
	});		
}

// 배열에 추가된 마커들을 지도에 표시하거나 삭제하는 함수입니다
function setMarkers(map) {
    for (var i = 0; i < da_makers.length; i++) {
    	da_makers[i].setMap(map);
    }            
}

// "마커 보이기" 버튼을 클릭하면 호출되어 배열에 추가된 마커를 지도에 표시하는 함수입니다
function showMarkers() {
    setMarkers(map)    
}

// "마커 감추기" 버튼을 클릭하면 호출되어 배열에 추가된 마커를 지도에서 삭제하는 함수입니다
function hideMarkers() {
    setMarkers(null);    
}

// 인포윈도우를 표시하는 클로저를 만드는 함수입니다 
function makeOverListener(map, marker, infowindow) {
    return function() {
        infowindow.open(map, marker);
    };
}

// 인포윈도우를 닫는 클로저를 만드는 함수입니다 
function makeOutListener(infowindow) {
    return function() {
        infowindow.close();
    };
}

// 라이딩 기록 공개/비공개 전환 버튼 클릭시 또는 스크랩 버튼 클릭 시
function change() {
	var mode = $("#bt_mode").text();
	if(mode=="스크랩하기"){	//스크랩 모드
		$.ajax({
			url : "/riding/scrap/"+$("#selected_rr_num").val()+"/"+userId,
			type : "post",
			cache : false,
			complete : function(data){
				if(data.responseText=="success"){
					alert('해당 코스를 스크랩하였습니다!');
				}else if(data.responseText=="fail"){
					alert('이미 스크랩한 코스입니다!');
				}else if(data.responseText=="failMyRR"){
					alert('나의 주행기록입니다...');
				}
			}
		});
	}else if(mode=="스크랩취소하기"){
		$.ajax({
			url : "/riding/scrap",
			type : "delete",
			data : {
				ss_rider : userId,
				ss_rnum : $("#selected_rr_num").val()
			},
			cache : false,
			complete : function(data){
				if(data.responseText=="success"){
					alert('해당 코스를 스크랩 취소하였습니다!');
				}
				location.reload();
			}
		});
	}else { // 공개/비공개 모드
		var open = $("#bt_rr_open").val();
		if(open=='1'){
			open = 0;
		}else if(open=='0'){
			if($("#rr_name").val()==null){
				alert('경로이름을 지정해주세요.');
				return;
			}
			open = 1;
		}
		$.ajax({
			url : "/riding/update",
			type : "put",
			data : {
				rr_num : $("#selected_rr_num").val(),
				rr_open : open
			},
			dataType : "json",
			cache : false,
			success : function(data) {},
			complete : function(data){
				if(data.responseText=='y'){
					$('#bt_mode').text("비공개로 전환하기");
					$("#bt_rr_open").val(1);
				}else if(data.responseText=='n'){
					$('#bt_mode').text("공개로 전환하기");
					$("#bt_rr_open").val(0);
				}else {
					alert("에러발생!");
				}
			}
		});
	}
}

//rr_num으로 라이딩 기록 조회
function getRidingRecordByRR_Num(rr_num, mode){
	 $.ajax({
			url : "/getRidingRecordByRR_Num",
			type : "GET",
			data : {rr_num : rr_num},
			dataType : "json",
			cache : false, 
			success : function(data) {
				getReviewsByRR_Num(data.rr_num);
				$("#chrrnamebtn").hide();
				if(mode=="course"){
					$("#cif_name").text(data.rr_name); 
					$("#cif_total").text( (parseInt(data.rr_distance)/1000).toString().substring(0,3)+ " km"  ); 
					$("#cif_avg").text(data.rr_avgspeed +" km/h"); 
					$("#cif_high").text(data.rr_high+" m"); 
					$("#cif_like").text(data.rr_like);
					$('#bt_mode').text("스크랩하기");
				}else if(mode=="ridingrecord"){
					$("#cif_name").text(data.rr_name); 
					$("#cif_total").text( (parseInt(data.rr_distance)/1000).toString().substring(0,3)+ " km" ); 
					$("#cif_avg").text(data.rr_avgspeed+" km/h"); 
					$("#cif_high").text(data.rr_high+" m"); 
					$("#cif_like").text(data.rr_like);
					$("#cif_date").text(data.rr_date);
					$("#cif_time").text( hourminsec(data.rr_time) );
					$("#cif_top").text(data.rr_topspeed+" km/h");
					$("#cif_break").text( hourminsec(data.rr_breaktime) );
					$("#chrrnamebtn").show();
					hourminsec(data.rr_time);
					if(data.rr_open==1){
						$('#bt_mode').text("비공개로 전환하기");
					}else{
						$('#bt_mode').text("공개로 전환하기");
					}
				}else if(mode=="scrap"){
					$("#cif_name").text(data.rr_name); 
					$("#cif_total").text( (parseInt(data.rr_distance)/1000).toString().substring(0,3)+ " km" ); 
					$("#cif_avg").text(data.rr_avgspeed+" km/h"); 
					$("#cif_high").text(data.rr_high+" m"); 
					$("#cif_like").text(data.rr_like);
					$("#cif_date").text(" - ");
					$("#cif_time").text(" - ");
					$("#cif_top").text(" - ");
					$("#cif_break").text(" - ");
					$("#bt_mode").text("스크랩취소하기");
				}
				
				$('#courseInfo').show();
				$("#bt_rr_open").val(data.rr_open);
				$("#selected_rr_num").val(data.rr_num);
				$("#rr_name").val(data.rr_name);
				loadHashTag(data.rr_num);
			}
	});
 }


//시간단위 초 > 시분초
function hourminsec(text){
	var sec = parseInt(text);
	var hour = parseInt(sec / (60*60));
	var min = parseInt( ( sec - (hour*60*60) ) / 60 );
	sec = sec - (hour*60*60) - ( min*60 );
	if(hour==0 && min == 0){
		return sec+"초";
	}else if(hour==0 && min != 0){
		return min+"분 "+sec+"초";
	}else {
		return hour+"시 "+min+"분 "+sec+"초";
	}
}

//rr_num으로 지도에 경로 그리기
 function drawPolylineByRR_Num(polyline, rr_num){
	 $.ajax({
		 url : "/getGpxByRR_Num",
		 type : "GET",
		 data : {rr_num : rr_num},
		 dataType : "json",
		 cache : false,
		 success : function(data) {
				
				var point = data.infos;
				var linePath = [];
				
				// 지도를 재설정할 범위정보를 가지고 있을 LatLngBounds 객체를 생성합니다
				var bounds = new kakao.maps.LatLngBounds();
		
				for(var i=0;i<point.length;i++){
					var location = new kakao.maps.LatLng(point[i].lat, point[i].lon);
					linePath.push(location);
					bounds.extend(location);
				}
				// 지도에 선을 표시합니다 
				polyline.setPath(linePath);
				// 지도에 선을 표시합니다 
				polyline.setMap(map);
				// 맵을 이동시킵니다.
				map.setBounds(bounds);
		}
	});
 }
 
//rr_num으로 리뷰 조회
 function getReviewsByRR_Num(rr_num){
	 $("#courseReview ul").html("");
	 $.ajax({
		 url : "/riding/reviews/"+rr_num,
		 type : "GET",
		 dataType : "json",
		 cache : false,
		 success : function(data) {
			for(var i=0;i<data.length;i++){
				var code = "<li class='comment'><div class='vcard bio'>";
				
				// 유저 이미지
				if(data[i].cr_images != null){
					code += "<img src='/test/images/person_1.jpg' alt='Image placeholder'>";
				}else {
					code += "<img src='/imgs/test001.png' alt='Image placeholder'>";
				}
				
				code += "</div><div class='comment-body'>"
					+"<h3>"+data[i].cr_rider+"</h3>"
					+"<div class='meta mb-2'>"+data[i].cr_time+"</div>"
					+"<p>"+data[i].cr_content+"</p>";
					
				if(data[i].cr_rider==userId){
					code += "<p><a href='#' class='reply' onclick='updateReview("+data[i].cr_num+")'>수정</a> <a href='#' class='reply' onclick='deleteReview("+data[i].cr_num+")'>삭제</a></p>";
				}
				if(data[i].cr_images != null){
					code += "<img class='reviewimg' src='/image/review/"+data[i].cr_images+"'>";
				}
				code += "</div></li>";
				$("ul.comment-list").append(code);
			}	
		}
	});
 }
 
 function moveMyInfo(){
	 var form = document.createElement("form");
	form.setAttribute("method", "Post");  
	form.setAttribute("action", "/riding/course"); 
  	var hiddenField = document.createElement("input");
	hiddenField.setAttribute("type", "hidden");
	hiddenField.setAttribute("name", "rider");
    hiddenField.setAttribute("value", $("#hiddenID").val());
     form.appendChild(hiddenField);
     document.body.appendChild(form);
     form.submit();
 }
 function moveSearchCourse(){
	 location.href = "/riding/search";
 }

 var postmode = false; // 위험지역 작성 상태인지 아닌지 판단
 
 //위험지역 등록 눌렀을 때
 function show_post_DA(){
	 $("#box_post_DA").show();
	 $("#form_post_DA")[0].reset();
	 postmode = true;
	 
	 // 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출합니다
	 kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
		 if(!postmode){
			 return;
		 }
	 	// 클릭한 위도, 경도 정보를 가져옵니다 
	     var latlng = mouseEvent.latLng; 
	     
	 	// 마커 위치를 클릭한 위치로 옮깁니다
	    da_Point.setPosition(latlng);
	    da_Point.setMap(map);
	 	
	    $("#DA_Lat").val(latlng.getLat());
	 	$("#DA_Lon").val(latlng.getLng());

	 	searchDetailAddrFromCoords(mouseEvent.latLng, function(result, status) {
	         if (status === kakao.maps.services.Status.OK) {
	         	// 지번 주소정보
	         	var detailAddr = result[0].address.address_name;
	         	var addr = detailAddr.split(" ");
	         	// 시 + 군구
	         	$("#DA_addr").val(addr[0]+" "+addr[1]);
	         }
	 	});
	 	
	 	function searchDetailAddrFromCoords(coords, callback) {
		    // 좌표로 법정동 상세 주소 정보를 요청합니다
	 		geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
	 	}
	 });
 }
 
 //위험지역등록 취소 눌렀을 때
 function cancel_post_DA(){
	 $("#box_post_DA").hide();
	 da_Point.setMap(null);
	 postmode = false;
 }
 
 //리뷰 등록버튼 눌렀을 때
 function postReview(){
	 var content = $("#cr_content").val();
	 if(jQuery.trim(content)==""||(content==null)){
		 alert('한줄평을 작성하지 않았습니다.');
		 $("#cr_content").val("");
		 $("#cr_content").focus();
		 return;
	 }
	 
	 $("#cr_rider").val(userId);
	 $("#cr_rnum").val($("#selected_rr_num").val());
	 
	 var form = $("#postReview")[0];
	 var formdata = new FormData(form);
	 
	 
	 $.ajax({
		 url : "/riding/review",
		 type : "POST",
		 data : formdata,
		 processData: false,
         contentType: false,
		 cache : false,
		 success : function(data) {
			 $("#cr_content").val("");
			 getReviewsByRR_Num($("#selected_rr_num").val());
		}
	});
 }


 
 //리뷰 삭제
 function deleteReview(cr_num){
	 $.ajax({
		 url : "/riding/review",
		 type : "delete",
		 data : { cr_num : cr_num },
		 cache : false,
		 success : function(data) {
			 getReviewsByRR_Num($("#selected_rr_num").val());
		}
	});
 }
 
 //리뷰 수정
 function updateReview(cr_num){
	 var newreply = prompt("수정할 댓글을 입력하세요");
	 if(newreply==null || newreply == ""){
		 return;
	 }
	 var check = confirm(newreply+"로 수정하시겠습니까?");
	 if(check){
		 $.ajax({
			 url : "/riding/review",
			 type : "put",
			 data : { 
				 cr_num : cr_num,
				 cr_content : newreply
			 },
			 cache : false,
			 success : function(data) {
				 getReviewsByRR_Num($("#selected_rr_num").val());
			}
		});
	 }
	 
 }
 
//추천 눌렀을 때
 function like(){
	 var rr_num = $("#selected_rr_num").val();
	 $.ajax({
		 url : "/riding/like",
		 type : "post",
		 data : { 
			 ls_rnum : rr_num,
			 ls_rider : userId
		 },
		 success : function(data) {
			 if(data=="success"){
				 alert("해당 코스를 추천하였습니다.");
			 }else if(data=="myrr"){
				 alert("자신의 코스는 추천할 수 없습니다.");
			 }else if(data=="already"){
				 alert("이미 추천한 코스입니다.");
				 var check = confirm("추천을 취소하시겠습니까?");
				 if(check){
					 unlike(rr_num);
				 }
			 }
		}
	});
 }
 
 function unlike(rr_num){
	 $.ajax({
		 url : "/riding/like",
		 type : "delete",
		 data : { 
			 ls_rnum : rr_num,
			 ls_rider : userId
		 },
		 cache : false,
		 success : function(data) {
			 if(data=="success"){
				 alert("해당 코스에 대한 추천을 취소하였습니다.");
			 }
		}
	});
 }
 
 //위험지역 등록신청 버튼 클릭
 function regDA(){
	 
	 var form = $("#form_post_DA")[0];
	 var formdata = new FormData(form);
	 
	 $.ajax({
		 url : "/dangerousArea",
		 type : "post",
		 data : formdata,
		 processData: false,
         contentType: false,
		 cache : false,
		 success : function(data) {
			 alert("위험지역 등록을 완료하였습니다.");
			 hideMarkers();
			 getDA();
			 cancel_post_DA();
		}
	});
 }
 
 //해시태그 로딩
function loadHashTag(rr_num){
	$(".hashtag").html("");
	$.ajax({
		url : "/info/riding/tag/"+rr_num,
		type : "get",
		success : function(data) {
			console.log(data);
			
			for(var i=0;i<data.length;i++){
				var span = document.createElement("span");
				span.setAttribute("class", "usertag");
				var newA = document.createElement("a");
				newA.innerHTML = data[i].ts_tag;
				newA.setAttribute("href", "#");
				span.append(newA);
				if(data[i].ts_rider==$("#DARider").val()){
					
					var newBtn = document.createElement("button");
					newBtn.innerHTML="X";
					newBtn.setAttribute("onclick", "removeTag("+ data[i].ts_num +","+rr_num+")");
					span.append(newBtn);
				}
				
				$(".hashtag").append(span);
			}
		}
	});
}

//해시태그 삭제
function removeTag(ts_num,rr_num){
	console.log(ts_num);
	$.ajax({
		url : "/info/riding/tag/"+ts_num,
		type : "delete",
		success : function(data) {
			console.log(data);
			loadHashTag(rr_num);
		}
	});
}

function wrapWindowByMask() {
        //화면의 높이와 너비를 구한다.
        var maskHeight = $(document).height(); 
        var maskWidth = $(window).width();

        //문서영역의 크기 
        console.log( "document 사이즈:"+ $(document).width() + "*" + $(document).height()); 
        //브라우저에서 문서가 보여지는 영역의 크기
        console.log( "window 사이즈:"+ $(window).width() + "*" + $(window).height());        

        //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
        $('#mask').css({
            'width' : maskWidth,
            'height' : maskHeight
        });

        //애니메이션 효과
        //$('#mask').fadeIn(1000);      
        $('#mask').fadeTo("slow", 0.5);
    }

    function popupOpen() {
        $('.layerpop').css("position", "absolute");
        //영역 가운에데 레이어를 뛰우기 위해 위치 계산 
        $('.layerpop').css("top",(($(window).height() - $('.layerpop').outerHeight()) / 2) + $(window).scrollTop());
        $('.layerpop').css("left",(($(window).width() - $('.layerpop').outerWidth()) / 2) + $(window).scrollLeft());
        $('.layerpop').draggable();
        $('#layerbox').show();
    }

    function popupClose() {
        $('#layerbox').hide();
        $('#mask').hide();
    }

    function goDetail() {

        /*팝업 오픈전 별도의 작업이 있을경우 구현*/ 

        popupOpen(); //레이어 팝업창 오픈 
        wrapWindowByMask(); //화면 마스크 효과 
    }
    
    function saveTag(){
    console.log("----InsertTag-------");
    console.log($("#selected_rr_num").val());
    console.log($("#ts_rider").val());
    console.log($("#ts_tag").val());
    
    	$.ajax({
			url : "/info/riding/tag",
			type : "post",
			data : {
				ts_rnum : $("#selected_rr_num").val(),
				ts_rider : $("#ts_rider").val(),
				ts_tag : $("#ts_tag").val()
			},
			success : function(data) {
				console.log(data);
				$('#layerbox').hide();
        		$('#mask').hide();
        		loadHashTag($("#selected_rr_num").val());
			}
		});
    }

