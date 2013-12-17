/**
* Extends the Annotorious popup with the Semantic Tagging field.
* @param {Object} annotator the annotator (provided by the Annotorious framework)
*/
annotorious.plugin.SemanticTagging.prototype._extendPopup = function(annotator) {
  annotator.popup.addField(function(annotation) {
    var popupContainer = document.createElement('div');
    if (annotation.tags) {
      jQuery.each(annotation.tags, function(idx, tag) {
        var el = document.createElement('a');
        el.href = '#';
        el.className = 'semtagging-tag semtagging-popup-tag';
        el.innerHTML = tag.title;

        if (tag.status == 'rejected')
          jQuery(el).addClass('rejected');
        else
          jQuery(el).addClass('accepted');

        popupContainer.appendChild(el);
      });
    }
    return popupContainer;
  });
}
