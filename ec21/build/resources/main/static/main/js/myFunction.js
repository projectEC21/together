/********************
 * 모달과 관련된 함수
 *************************/

// modalControl.js
$(document).ready(function () {
    $('#inquiry').click(function () {
        $('#myModal').modal('show');  // Bootstrap 모달을 수동으로 열기
    });
});


// inquiry modalControl.js
$(document).ready(function () {
    $('#inquiryTitle').click(function () {
        $('#inquiryDetail').modal('show');  // Bootstrap 모달을 수동으로 열기
    });
});

/*********************************************** 
 * saved-icon 을 누르면 색이 있는 별로 되게 하는 스크립트
/************************************************ */
// $(document).ready(function() {

//     // 이벤트 위임하는 것
//     $(document).on('click', '.saved-icon', function(event) {
//         event.preventDefault();
//         var icon = $(this).find('i');

//         if (icon.hasClass('ti-star')) {
//             icon.removeClass('ti-star').addClass('fa fa-star');
//         } else {    
//             icon.removeClass('fa fa-star').addClass('ti-star');
//         }
//     });
// });

/**********************************
 * IP 주소 마지막 주소 * 처리
 ***********************************/

// 문서 로딩 시 함수 실행
document.addEventListener('DOMContentLoaded', function () {
    maskLastThreeChars();
});

function maskLastThreeChars() {
    var ipElements = document.querySelectorAll('.remoteIp'); // 'remoteIp' 클래스를 가진 모든 요소 선택
    ipElements.forEach(function (ipElement) { // 선택한 모든 요소에 대해 반복
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


/**********************************
 * 메인 화면에 국기 이미지 넣기 ----- 아직 미완성
 ***********************************/

// $(".product-seller").each(function() {
//     let countryName = $(this).data("country");
//     console.log("Country name:", countryName);

//     if (!countryName) { // 국가 이름이 누락된 경우 처리
//         console.error("Country data is missing.");
//         // 기본 국기 이미지 설정
//         $("#countryFlag").attr("src", "https://flagsapi.com/default/flat/64.png");
//         $("#countryFlag").attr("alt", "Default Country Flag");
//     } else {
//         // AJAX 요청을 통해 국가 정보를 가져옴
//         $.ajax({
//             url: `https://restcountries.com/v3.1/name/${countryName}`, // 국가 이름 기반 요청
//             method: 'GET',
//             success: function(data) {
//                 if (data && data.length > 0) { // 데이터가 제대로 전달되었는지 확인
//                     let countryCode = data[0].cca2; // 국가 코드
//                     let flagImageUrl = `https://flagsapi.com/${countryCode}/flat/64.png`; // 국기 이미지 URL 생성
//                     $("#countryFlag").attr("src", flagImageUrl); // 이미지 요소에 국기 설정
//                     $("#countryFlag").attr("alt", `${countryName} Flag`); // 대체 텍스트 설정
//                 } else { // 데이터가 비어있는 경우 처리
//                     console.warn("Country data not found, using default.");
//                     $("#countryFlag").attr("src", "https://flagsapi.com/default/flat/64.png");
//                     $("#countryFlag").attr("alt", "Default Country Flag");
//                 }
//             },
//             error: function() { // AJAX 요청에 오류가 발생한 경우 처리
//                 console.error("Error retrieving country data, using default.");
//                 $("#countryFlag").attr("src", "https://flagsapi.com/default/flat/64.png");
//                 $("#countryFlag").attr("alt", "Default Country Flag");
//             }
//         });
//     }
// });

// $(".product-seller").each(function() {
//     let countryName = $(this).data("country");

//     if (!countryName) { // 데이터가 없을 경우 처리
//         console.error("Country data is missing."); // 오류 메시지 출력
//         // 여기서 중단하지 않고 계속 실행
//     } else {
//         // AJAX 요청을 통해 국가 데이터를 가져옴
//         $.ajax({
//             url: `https://restcountries.com/v3.1/name/${countryName}`,
//             method: 'GET',
//             success: function(data) {
//                 if (data && data.length > 0) { // 데이터가 제대로 반환되었는지 확인
//                     let countryCode = data[0].cca2; // 국가 코드 가져오기
//                     let flagImageUrl = `https://flagsapi.com/${countryCode}/flat/64.png`;
//                     // 국기 이미지 설정
//                     $("#countryFlag").attr("src", flagImageUrl).attr("alt", `${countryName} Flag`);
//                     console.log(countryCode);
//                 } else {
//                     console.warn("Country data not found."); // 데이터가 없을 때 경고
//                 }
//             },
//             error: function() { // AJAX 요청에 오류가 발생한 경우 처리
//                 console.error("Error retrieving country data.");
//             }
//         });
//     }
// });

/**********************************
 * 메인 화면에 국기 이미지 넣기 -----  완성
 ***********************************/
$(document).ready(function () {
    $('.product-seller').each(function () {
        var countryName = $(this).data("country");
        var $this = $(this); // 현재 .product-seller 요소를 $this 변수에 저장

        if (countryName) {
            // AJAX 요청을 통해 국가 정보를 가져옵니다.
            $.ajax({
                url: `https://restcountries.com/v3.1/name/${countryName}`,
                method: 'GET',
                success: function (data) {
                    if (data && data.length > 0) {
                        var countryCode = data[0].cca2.toLowerCase();
                        var flagImageUrl = `https://flagcdn.com/${countryCode}.svg`;
                        // $this 범위 내의 .countryFlag 이미지 src를 업데이트합니다.
                        $this.find(".countryFlag").attr('src', flagImageUrl).attr('alt', `${countryName} Flag`);
                    } else {
                        console.warn('No data for country: ' + countryName);
                        $this.find(".countryFlag").attr('src', '').attr('alt', 'Flag not available');
                    }
                },
                error: function () {
                    console.error('Error retrieving country data.');
                    $this.find(".countryFlag").attr('src', '').attr('alt', 'Error loading flag');
                }
            });
        }
    });
});

/**********************************
 * productsDetail -----  완성
 ***********************************/$(document).ready(function () {
    // 각 제품의 원산지를 기반으로 국기 이미지를 가져옵니다.
    $('.product-desc').each(function () {
        let originContainer = $(this).find('.origin');
        let countryName = originContainer.find('.country-name').text();
        let flagContainer = originContainer.find('.flag-container');

        // 국가 이름을 기반으로 AJAX 요청을 통해 국가 정보를 가져옵니다.
        if (countryName) {
            $.ajax({
                url: `https://restcountries.com/v3.1/name/${countryName}`,
                method: 'GET',
                success: function (data) {
                    if (data && data.length > 0) {
                        let countryCode = data[0].cca2.toLowerCase();
                        let flagImageUrl = `https://flagcdn.com/${countryCode}.svg`; // 변경된 API 사용
                        flagContainer.html(`<img src="${flagImageUrl}" alt="${countryName} Flag" style="width:20px; height:20px;">`);
                    } else {
                        console.warn("Country data not found.");
                        flagContainer.html(""); // 국가 데이터가 없을 경우 플래그 컨테이너를 비웁니다.
                    }
                },
                error: function () {
                    console.error("Error retrieving country data.");
                    flagContainer.html(""); // AJAX 요청 오류 시 플래그 컨테이너를 비웁니다.
                }
            });
        }
    });
});
