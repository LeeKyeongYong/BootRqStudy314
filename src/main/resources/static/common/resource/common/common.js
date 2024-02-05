toastr.options = {
    closeButton: true,
    debug: false,
    newestOnTop: true,
    progressBar: true,
    positionClass: "toast-top-right",
    preventDuplicates: false,
    onclick: null,
    showDuration: "300",
    hideDuration: "1000",
    timeOut: "5000",
    extendedTimeOut: "1000",
    showEasing: "swing",
    hideEasing: "linear",
    showMethod: "fadeIn",
    hideMethod: "fadeOut"
};

function parseMsg(msg) {
    const [pureMsg, ttl] = msg.split(";ttl=");

    const currentJsUnixTimestamp = new Date().getTime();

    if (ttl && parseInt(ttl) + 5000 < currentJsUnixTimestamp) {
        return [pureMsg, false];
    }

    return [pureMsg, true];
}

function toastMsg(isNotice, msg) {
    if (isNotice) toastNotice(msg);
    else toastWarning(msg);
}

function toastNotice(msg) {
    const [pureMsg, needToShow] = parseMsg(msg);

    if (needToShow) {
        toastr["success"](pureMsg, "알림");
    }
}

function toastWarning(msg) {
    const [pureMsg, needToShow] = parseMsg(msg);

    if (needToShow) {
        toastr["warning"](pureMsg, "경고");
    }
}

// 어떠한 기능을 살짝 늦게(0.1 초 미만)
function setTimeoutZero(callback) {
    setTimeout(callback);
}

$(function () {
    $('select[value]').each(function (index, el) {
        const value = $(el).attr('value');
        if (value) $(el).val(value);
    });

    $('a[method="post"]').click(function (e) {
        let onclickAfter = null;

        eval("onclickAfter = function() { " + $(this).attr('onclick-after') + "}");

        if (!onclickAfter()) return false;

        const action = $(this).attr('href');
        const csfTokenValue = $("meta[name='_csrf']").attr("content");

        // 동적으로 폼을 만든다.
        const $form = $(`<form action="${action}" method="POST"><input type="hidden" name="_csrf" value="${csfTokenValue}"></form>`);
        $('body').append($form);
        $form.submit();

        return false;
    });

    $('a[method="post"][onclick]').each(function (index, el) {
        const onclick = $(el).attr('onclick');

        $(el).removeAttr('onclick');

        $(el).attr('onclick-after', onclick);
    });
});