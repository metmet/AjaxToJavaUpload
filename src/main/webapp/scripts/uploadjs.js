/**
* UPLOADJS
**/
var overMaxSize = false;
var fileValid	= false;
var file1Valid	= false;
var file2Valid	= false;

var maxSizeMBSingleFile	= 2;
var maxSizeFileUploadSingleFile = (maxSizeMBSingleFile * 1024 * 1024);
var extensionsImg	 = ["jpg","jpeg","gif","png","bmp"];

function clearFieldFile(idFile){
	if ($.browser.msie) {
		var fld = document.getElementById(idFile);
		fld.form.reset();
		fld.focus();
	}else{
		$("#"+idFile).val("");
	}
}

UPLOADJS = {
	submit : function(thisForm, c, maxSize) {
		UPLOADJS.form(thisForm, UPLOADJS.frame(c));
		if (c && typeof(c.onStart) == 'function') {
			return c.onStart();
		} else {
			return true;
		}
	},

	frame : function(c) {

		var idNameIframe = 'id' + Math.floor(Math.random() * 100000);
		var divDoc = document.createElement('DIV');
		divDoc.innerHTML = '<iframe src="about:blank" id="'+idNameIframe+'" name="'+idNameIframe+'" style="display:none" onload="UPLOADJS.loaded(\''+idNameIframe+'\')"></iframe>';
		document.body.appendChild(divDoc);
 
		var i = document.getElementById(idNameIframe);
		if (c && typeof(c.onComplete) == 'function') {
//console.log(" :: :: :: :: :: ");
//console.log(i);
//console.log(c.onComplete);
			i.onComplete = c.onComplete;
		}
		return idNameIframe;
	},
 
	form : function(f, name) {
		f.setAttribute('target', name);
	},
 

 
	loaded : function(id) {
//console.log("loaded"+id);
		var i = document.getElementById(id);
		if (i.contentDocument) {
			var d = i.contentDocument;
		} else if (i.contentWindow) {
			var d = i.contentWindow.document;
		} else {
			var d = window.frames[id].document;
		}
		if (d.location.href == "about:blank") {
			return;
		}
 
		if (typeof(i.onComplete) == 'function') {
			i.onComplete(d.body.innerHTML);
		}
	}
}

$.fn.checkFileTypeAndSize = function(options) {

    var defaults = {
        allowedExtensions: [],
        success: function() {},
        errorFileType: function() {},
        errorFileSize: function() {}
    };
    options = $.extend(defaults, options);

    return this.each(function() {

        $(this).on('change', function() {

            var value = $(this).val(),
                file = value.toLowerCase(),
                extension = file.substring(file.lastIndexOf('.') + 1);

            if ($.inArray(extension, options.allowedExtensions) == -1) {
                options.errorFileType();
				fileValid = false;
                $(this).focus();
            } else {
				fileValid = true;
				try{
					// IE manda in Exception questa riga 
					var fileSize = this.files[0].size
					if (fileSize > maxSizeFileUploadSingleFile){
						overMaxSize = true;
						options.errorFileSize();
					}else{
						overMaxSize = false;
	                    options.success();
					}

				}catch(e){
					if ($.browser.msie) {
						if (jQuery.inArray(extension, extensionsImg) == 0){// controllo che non sia un pdf 
							$('#myImage').attr('src',file);  
						    setTimeout(function(){
								var imgbytes = document.getElementById('myImage').fileSize;  
								//var imgkbytes = Math.round(parseInt(imgbytes)/1024);  
//alert("imgbytes ["+imgbytes+"] - maxSizeFileUploadSingleFile ["+maxSizeFileUploadSingleFile+"]");
								if (imgbytes != -1 && imgbytes > maxSizeFileUploadSingleFile){
									overMaxSize = true;
		    						options.errorFileSize();
								}else{
									overMaxSize = false;
		    	                    options.success();
								}
						    }, 1000);

						}else{
							//è un pdf e in IE non possiamo eseguire il controllo lato client sulla size lo demandiamo al server
							overMaxSize = false;
						}
					}else{
	                    options.errorFileType();
						fileValid = false;// siamo in una Exception non gestita
					}
				}
            }
        });
    });
};
