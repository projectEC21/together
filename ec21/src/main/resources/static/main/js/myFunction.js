// 모달과 관련된 함수

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

// saved-icon 을 누르면 색이 있는 별로 되게 하는 스크립트
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

