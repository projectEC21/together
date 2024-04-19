// 모달과 관련된 함수

// modalControl.js
$(document).ready(function() {
    $('#inquiry').click(function() {
        $('#myModal').modal('show');  // Bootstrap 모달을 수동으로 열기
    });
});

// saved-icon 을 누르면 색이 있는 별로 되게 하는 스크립트
$(document).ready(function() {
    $('#saved-icon').click(function(event) {
        event.preventDefault(); // 기본 동작 방지
        var icon = $(this).find('i'); // .saved-icon 내부의 <i> 태그를 찾음
    
        // 클래스 변경
        icon.removeClass('ti-star'); // 기존 클래스 제거
        icon.addClass('fa fa-star'); // 새 클래스 추가
    });
});
