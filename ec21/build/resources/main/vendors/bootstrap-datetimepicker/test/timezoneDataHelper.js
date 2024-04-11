<<<<<<< HEAD
(function () {
    'use strict';
    $.ajax('node_modules/moment-timezone/data/packed/latest.json', {
        success: function (data) {
            moment.tz.load(data);
        },
        method: 'GET',
        dataType: 'json',
        async: false
    });
}());
=======
(function () {
    'use strict';
    $.ajax('node_modules/moment-timezone/data/packed/latest.json', {
        success: function (data) {
            moment.tz.load(data);
        },
        method: 'GET',
        dataType: 'json',
        async: false
    });
}());
>>>>>>> 1a1ecd553fc320897f03b38db9609eb13cd29bc3
