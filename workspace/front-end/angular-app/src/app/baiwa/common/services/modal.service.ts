import { Injectable } from "@angular/core";
import { Modal } from "models/";
import { Observable } from "rxjs";

declare var $: any;

@Injectable()
export class ModalService {

    constructor() { }

    show(name: string) {
        $(`#${name}`).modal('show');
    }

    alert(obj: Modal) {
        const id = obj.success ? "alert-success" : "alert";
        // message
        $(`#${id} .header`).html(obj.title || "แจ้งเตือน");
        $(`#${id} .content`).html(obj.msg || "");
        // color
        $(`#${id} .header`).removeClass('notification');
        $(`#${id} .header`).addClass(obj.color || '');
        // size
        $(`#${id}`).removeClass('small');
        $(`#${id}`).addClass(obj.size || 'small');
        // active
        $(`#${id}`).modal({ centered: false, autofocus: false }).modal('show');
    } z

    alertWithAct(obj: Modal, func?: Function) {
        const id = "alert-func";
        // message
        $(`#${id} .header`).html(obj.title || "แจ้งเตือน");
        $(`#${id} .content`).html(obj.msg || "");
        // color
        $(`#${id} .header`).removeClass('notification');
        $(`#${id} .header`).addClass(obj.color || '');
        // size
        $(`#${id}`).removeClass('small');
        $(`#${id}`).addClass(obj.size || 'small');
        // active
        $(`#${id}`)
            .modal({
                centered: false,
                autofocus: false,
                onApprove: (element) => {
                    if (func) {
                        func(true)
                    }
                },
            })
            .modal('show');
    }

    confirm(func: Function, obj: Modal) {
        // message
        $('#confirm .header').html(obj.title || "การยืนยัน");
        $('#confirm .content').html(obj.msg || "");
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