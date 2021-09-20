$('#flexCheckDefault').change(function () {
    $('.student-rows').prop('checked',this.checked);
});

$('.student-rows').change(function () {
    if ($('.student-rows:checked').length == $('.student-rows').length){
        $('#flexCheckDefault').prop('checked',true);
    }
    else {
        $('#flexCheckDefault').prop('checked',false);
    }
});