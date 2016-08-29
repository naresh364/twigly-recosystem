$ ->
    imageUrl = $('#imageURL').val()
    $("#menu-item-image").fileinput
        browseClass: "btn btn-primary btn-block"
        showCaption: false
        showUpload: false
        allowedPreviewTypes: ['image']
        allowedFileTypes: ['image']
        dropZoneEnabled: false
        initialPreview: ["<img src='"+imageUrl+"' class='file-preview-image' alt='Menu Item' title='Menu Item'>"]
        layoutTemplates:
            footer: '<div class="file-thumbnail-footer">\n' +
                    '    <div class="file-caption-name">{caption}</div>\n' +
                    '</div>';
        maxFileSize: 300
        msgSizeTooLarge: 'File "{name}" (<b>{size} KB</b>) exceeds maximum allowed upload size of <b>{maxSize} KB</b>. Please compress the image and retry your upload!'
        uploadUrl: $("#image-upload-url").attr('value')


$ ->
    $('#menu-item-image').on "filebatchselected", (event, files) ->
        $(this).fileinput("upload")


$ ->
    $('#menu-item-image').on "fileuploaded", (event, data, previewId, index) ->
        $('#imageURL').val(data.response.image_path)

$ ->
    $('#menu-item-image').on "filecleared", (event) ->
        $('#imageURL').val('')