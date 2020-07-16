PhotoDetailModal = function () {
    let me = this;

    $("#btnEditAdd").attr({
        onclick: function () {
            $(".takenPlace").removeAttr("readOnly");
        }
    });

    $("#btnEditLabel").attr({
        onclick: function () {
            $(".inputPhotoLabel").removeAttr("readOnly");
        }
    });

    $("#btnOK").attr({
        onclick: function () {
           me._updateInfo();
        }
    });

    $("#btnGoPublic").attr({
        onclick: function () {
            me._goPublic();
        }
    });
};

PhotoDetailModal.prototype._updateInfo = function () {

};

PhotoDetailModal.prototype._goPublic = function () {

};