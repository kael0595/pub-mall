
const a = 1;

console.log(a);

$(document).ready(function () {
    $('.allCheck').click(function () {
        var checked = $('.allCheck').is(':checked');

        console.log(checked);

        if (checked) {
            $('input:checkbox').prop('checked', true);
        } else {
            $('input:checkbox').prop('checked', false);
        }
    });

    $('.btnPrimary').click(function (e) {
        if (!$('.check:checked').length || !$('.check2:checked').length) {
            e.preventDefault();
            alert('필수 체크박스를 선택해주세요.');
        }
    });
});