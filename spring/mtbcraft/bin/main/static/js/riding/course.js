//나의 기록 클릭시
$(".li_rr").click(function(){
	drawPolylineByRR_Num(polyline, this.id);
	$("#courseInfo").show();
	//rr_num을 이용하여 라이딩상세 기록 조회
	var mode = "ridingrecord";
	getRidingRecordByRR_Num(this.id, mode);
});

//스크랩코스 클릭시 
$(".li_sc").click(function(){
	drawPolylineByRR_Num(polyline, this.id);
	$("#courseInfo").show();
	//rr_num을 이용하여 라이딩상세 기록 조회
	var mode = "scrap";
	getRidingRecordByRR_Num(this.id, mode);
});

 // 코스명 변경 시
 function changeRR_Name(){
	 if($("#rr_name").val()==null || $("#rr_name").val() =="" || $("#rr_name").val() ==" "){
		 alert("코스 명을 입력한 후 버튼을 눌러주세요.");
		 return;
	 }
	 $.ajax({
			url : "/ridingrecord/rr_name",
			type : "put",
			data : {
				rr_num : $("#selected_rr_num").val(),
				rr_name : $("#rr_name").val()
			},
			dataType : "json",
			cache : false, 
			complete : function(data) {
				alert('코스 명을 변경하였습니다.');
			}
	});
 }