/********************
 * 모달과 관련된 함수
 *************************/ 

// modalControl.js
$(document).ready(function() {
    $('#inquiry').click(function() {
        $('#myModal').modal('show');  // Bootstrap 모달을 수동으로 열기
    });
});


// inquiry modalControl.js
$(document).ready(function() {
    $('#inquiryTitle').click(function() {
        $('#inquiryDetail').modal('show');  // Bootstrap 모달을 수동으로 열기
    });
});

/*********************************************** 
 * saved-icon 을 누르면 색이 있는 별로 되게 하는 스크립트
/************************************************ */
$(document).ready(function() {

    // 이벤트 위임하는 것
    $(document).on('click', '.saved-icon', function(event) {
        event.preventDefault();
        var icon = $(this).find('i');
        
        if (icon.hasClass('ti-star')) {
            icon.removeClass('ti-star').addClass('fa fa-star');
        } else {    
            icon.removeClass('fa fa-star').addClass('ti-star');
        }
    });
});

/**********************************
 * IP 주소 마지막 주소 * 처리
 ***********************************/

// 문서 로딩 시 함수 실행
document.addEventListener('DOMContentLoaded', function() {
    maskLastThreeChars();
});

function maskLastThreeChars() {
    var ipElements = document.querySelectorAll('.remoteIp'); // 'remoteIp' 클래스를 가진 모든 요소 선택
    ipElements.forEach(function(ipElement) { // 선택한 모든 요소에 대해 반복
        var remoteIp = ipElement.innerText; // 각 요소의 텍스트 가져오기
        if (!remoteIp) return; // IP 주소가 없을 경우 건너뛰기
        
        if (remoteIp.length <= 3) {
            // IP 주소가 3자리 이하일 경우 전부 ***로 마스킹
            ipElement.innerText = '***';
        } else {
            // IP 주소의 마지막 세 자리를 ***로 마스킹
            ipElement.innerText = remoteIp.slice(0, -3) + '.***';
        }
    });
}
