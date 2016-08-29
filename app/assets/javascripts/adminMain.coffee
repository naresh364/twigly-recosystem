$ ->
	$('#side-menu').metisMenu()

$ ->
	$(window).bind "load resize", ->
		topOffset = 50
		width = if this.window.innerWidth > 0 then this.window.innerWidth else this.screen.width
		if width < 768
			$('div.navbar-collapse').addClass('collapse')
			topOffset = 100
		else
			$('div.navbar-collapse').removeClass('collapse')

		height = if this.window.innerHeight > 0 then this.window.innerHeight else this.screen.height
		height = height - topOffset - 1
		if height < 1 then height = 1
		if height > topOffset then height = 10

$ ->
	url = window.location
	element = $('ul.nav a').filter ->
    	this.href == url || url.href.indexOf(this.href) == 0
    element = element.addClass('active').parent().parent().addClass('in').parent()
    if element.is('li') then element.addClass('active')

$ ->
	$(".confirmation").on "click", ->
		return confirm('Are you sure?')

$ ->
	$('[data-toggle="ajaxModal"]').on "click", (event) ->
		$('#ajaxModal').remove()
		event.preventDefault()
		$this = $(this)
		$remote = $this.data('remote') || $this.attr('href')
		$modal = $('<div class="modal fade" id="ajaxModal" tabindex="-1" role="dialog" aria-labelledby="editCity" aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									Loading please wait ...
								</div>
								<div class="modal-body">
									<div class="progress progress-striped active">
										<div class="progress-bar" style="width:100%">
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>')
		$('body').append($modal)
		$modal.modal({backdrop: true, keyboard: true})
		$m2 = $modal.find(".modal-content");
		$m2.load($remote)

root = exports ? this

root.updateClick = () ->
	$updateButton = $('a#update-button')
	$updateButton.remove()
	$('button#modal-close-button').remove()
	$m2 = $('#ajaxModal').find(".modal-content");
	$formData = $("form#modal-body-form").serialize()
	$m2.find('.modal-header').html("Updating.... Please wait ..")
	$m2.find('.modal-body').html('<div class="progress progress-striped active">
                                  	<div class="progress-bar" style="width:100%">
                                  	</div>
                                  </div>')
	$.ajax
		type: "POST"
		url: $updateButton.attr('href')
		data: $formData
		error: (jqXHR, textStatus, errorThrown) ->
			$m2.html(jqXHR.responseText)
		success: (data, textStatus, jqXHR) ->
			$m2.find('.modal-body').html("Update Successfull. Reloading page automatically...")
			location.reload()
		false
	false

