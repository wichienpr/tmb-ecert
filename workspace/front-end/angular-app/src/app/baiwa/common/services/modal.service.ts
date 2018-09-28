import { Injectable } from "@angular/core";

declare var $: any;

@Injectable()
export class ModalService {

    constructor() { }

    alert(obj: Modal) {
        const id = obj.success ? "alert-success" : "alert";
        // message
        $(`#${id} .header`).html(obj.title || "แจ้งเตือน");
        $(`#${id} .content`).html(obj.msg || "...");
        // color
        $(`#${id} .header`).addClass(obj.color || '');
        // size
        $(`#${id}`).removeClass('small');
        $(`#${id}`).addClass(obj.size || 'small');
        // active
        $(`#${id}`).modal('show');
    }

    confirm(func: Function, obj: Modal) {
        // message
        $('#confirm .header').html(obj.title || "การยืนยัน");
        $('#confirm .content').html(obj.msg || "...");
        $('#confirm .ui.positive').html(obj.approveMsg || 'ยืนยัน');
        $('#confirm .ui.negative').html(obj.rejectMsg || 'ยกเลิก');
        // color
        $('#confirm .header').removeClass('notification');
        $('#confirm .header').addClass(obj.color || '');
        // size
        $('#confirm').removeClass('small');
        $('#confirm').addClass(obj.size || 'small');
        // callback function
        $("#confirm")
            .modal({
                centered: false,
                autofocus: false,
                onApprove: (element) => {
                    func(true);
                },
                onDeny: (element) => {
                    func(false);
                },
                closable: false
            })
            .modal("show");
    }

}

export interface Modal {
    msg: string;
    title?: string;
    color?: string;
    success?: boolean;
    size?: string;
    approveMsg?: string;
    rejectMsg?: string;
}