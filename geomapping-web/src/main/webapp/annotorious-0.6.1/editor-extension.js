/**
* Extends the Annotorious editor with the Semantic Tagging field.
* @param {Object} annotator the annotator (provided by the Annotorious framework)
*/
annotorious.plugin.SemanticTagging.prototype._extendEditor = function(annotator) {
  var self = this,
      container = document.createElement('div'),
      idle_timeout,
      MIN_TEXT_LENGTH = 5, // minimum length annotation must have before being allowed to the NER server
      TRIGGER_CHARS = ". ,", // characters that force an NER lookup
      IDLE_THRESHOLD = 500; // NER is also done after IDLE_THRESHOLD milliseconds of key idleness

  container.className = 'semtagging-editor-container';

  // Adds a tag
  var addTag = function(annotation, topic, opt_css_class) {
    self._tags[topic.id] = topic;

    var link = document.createElement('a');
    link.style.cursor = 'pointer';
    link.className = 'semtagging-tag semtagging-editor-tag';
    link.innerHTML = topic.title;
    container.appendChild(link);

    var jqLink = jQuery(link);
    if (opt_css_class)
      jqLink.addClass(opt_css_class);

    jqLink.click(function() {
      if (!annotation.tags)
        annotation.tags = [];

      if (jqLink.hasClass('accepted')) {
        // Toggle to 'rejected'
        jqLink.toggleClass('accepted rejected');
        topic.status = 'rejected';
      } else if (jqLink.hasClass('rejected')) {
        // Toggle to 'don't care'
        jqLink.removeClass('rejected');
        delete topic.status;
        var idx = annotation.tags.indexOf(topic);
        if (idx > -1)
          annotation.tags.splice(idx, 1);
      } else {
        // Toggle to 'accepted'
        jqLink.addClass('accepted');
        delete topic.status;
        annotation.tags.push(topic);
      }
    });
  };

  // Does the NER lookup
  var doNER = function(annotation, text) {
    jQuery.getJSON(self._ENDPOINT_URI + text, function(data) {
      if (data.detectedTopics.length > 0) {
        jQuery.each(data.detectedTopics, function(idx, topic) {
          // Add to cached tag list and UI, if it is not already there
          if (!self._tags[topic.id])
            addTag(annotation, topic);
        });
      }
    });
  };

  // Restarts the keyboard-idleness timeout
  var restartIdleTimeout = function(annotation, text) {
    if (idle_timeout)
      window.clearTimeout(idle_timeout);
    
    idle_timeout = window.setTimeout(function() { doNER(annotation, text); }, IDLE_THRESHOLD);
  };

  // Add a key listener to Annotorious editor (and binds stuff to it)
  annotator.editor.element.addEventListener('keyup', function(event) {
    var annotation = annotator.editor.getAnnotation(),
        text = annotation.text;

    if (text.length > MIN_TEXT_LENGTH) {
      restartIdleTimeout(annotation, text);

      if (TRIGGER_CHARS.indexOf(text[text.length - 1]) > -1)
        doNER(annotation, text);
    }
  });

  // Final step: adds the field to the editor
  annotator.editor.addField(function(annotation) {
    self._tags = [];
    container.innerHTML = '';
    if (annotation && annotation.tags) {
      jQuery.each(annotation.tags, function(idx, topic) {
        var css_class = (topic.status == 'rejected') ? 'rejected' : 'accepted';
        addTag(annotation, topic, css_class);
      });
    }
    return container;
  });
}
