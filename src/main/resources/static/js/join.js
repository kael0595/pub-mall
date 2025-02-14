const idCheck = () => {

    $('.id_ok').hide();
    $('.id_already').hide();

    var username = $('#username').val();

    if (!username.trim()) {
        return;
    }

    $.ajax({
        type: "post",
        url: "/member/idCheck",
        data: {username: username},
        success: function (cnt) {

            if (cnt === 0) {
                $('.id_ok').show();
                $('.id_already').hide();
            } else {
                $('.id_already').show();
                $('.id_ok').hide();
            }
        },
        error: function () {
            alert("에러입니다.");
        }
    })
}

const pwCheck = () => {
    var password = $('#password').val();
    var passwordCnf = $('#passwordCnf').val();

    if (!password || !passwordCnf) {
        $('.pw_ok').hide();
        $('.pw_diff').hide();
        return;
    }

    if (password === passwordCnf) {
        $('.pw_ok').show();
        $('.pw_diff').hide();
    } else {
        $('.pw_ok').hide();
        $('.pw_diff').show();
    }
}

$(document).ready(function () {
    $('#emailCheck').on('click', function (event) {

        event.preventDefault();

        var email = $('#email').val();
        if (!email.trim()) {
            return;
        }
        $.ajax({
            type: "get",
            url: "/member/emailCheck",
            data: {email: email},
            success: function (response) {
                alert("이메일 인증번호를 전송했습니다.");
            },
            error: function () {
                alert("이메일 인증번호 전송에 실패했습니다. 다시 확인해주세요.");
            }
        });
    });
})


const authCheck = () => {
    var authCode = $('#authCode').val();

    if (!authCode.trim()) {
        return;
    }
    $.ajax({
        type: "get",
        url: "/member/authCheck",
        data: {authCode: encodeURIComponent(authCode)},
        success: function (response) {
            alert(response);
        },
        error: function () {
            alert("오류가 발생하였습니다.")
        }
    })
}

const validation = () => {
    let isValid = true;

    const requiredFields = [
        'username',
        'password',
        'passwordCnf',
        'name',
        'nickname',
        'email',
        'phone',
        'addr1',
        'addr2'
    ];

    requiredFields.forEach(fieldId => {
        const field = $(`#${fieldId}`);
        const value = field.val().trim();

        if (!value) {
            field.css('border', '2px solid red');
            isValid = false;
        } else {
            field.css('border', '');
        }
    });

    if (!isValid) {
        alert("비어있는 입력창이 있습니다.")
    }

    return isValid;
};

$('form').on('submit', function (event) {
    if (!validation()) {
        event.preventDefault();
    }

    $('#submitBtn').prop('disabled', true);
    return true;
});