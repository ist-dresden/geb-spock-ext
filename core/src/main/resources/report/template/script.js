$(document).ready(function(){
    $('.result.failed').closest('li').addClass('failed');
    $('.result.passed').closest('li').addClass('passed collapsed');
    $('.toggle').click(function(){
       $(this).closest('li').toggleClass('collapsed');
    });
    $('.image.link a').colorbox({rel:"screen",title:function(){
            return $(this).find('img').attr('title');
        }});
});
