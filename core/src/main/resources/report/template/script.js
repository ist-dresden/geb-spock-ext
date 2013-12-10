$(document).ready(function(){
    $('.result.failed').closest('li').addClass('failed');
    $('.result.passed').closest('li').addClass('passed collapsed');
    $('.toggle').click(function(){
       $(this).closest('li').toggleClass('collapsed');
    });
});
