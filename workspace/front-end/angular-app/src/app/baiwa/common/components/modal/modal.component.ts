import {
    Component,
    OnDestroy,
    Input,
    Output,
    EventEmitter,
    ContentChild,
    TemplateRef,
    AfterViewInit
} from "@angular/core";
import { Modal } from "models/";

declare var $: any;

@Component({
    selector: "ui-modal",
    templateUrl: "./modal.component.html",
    styleUrls: ["./modal.component.css"],
    exportAs : "ui-modal",
})
export class ModalComponent implements AfterViewInit, OnDestroy {
    /**
     * Contents inside selector
     */
    @ContentChild(TemplateRef) templateRef: TemplateRef<any>;

    /**
     * string of modal
     */
    @Input() modal: Modal;

    /**
     * works on type = 'confirm'
     * @return true | false
     */
    @Output() approve: EventEmitter<boolean>;
    @Output() reject: EventEmitter<boolean>;
    @Output() modalevent: EventEmitter<string>;

    constructor() {
        this.approve = new EventEmitter<boolean>();
        this.reject = new EventEmitter<boolean>();
        this.modalevent = new EventEmitter<string>();
    }

    ngAfterViewInit() {
        const onShowfn = ()=>{
            this.modalevent.emit(MODAL_EVENT.ONSHOW)
        };
        // On initial modal
        $(`#${this.modal.modalId}`).modal({ closable: false, centered: false, autofocus: false,allowMultiple:true ,onShow: onShowfn});
    }
    
    ngOnDestroy() {
        // On leave page to remove modal
        $(`#${this.modal.modalId}`).remove();
    }

    onApproved(): void {
        this.approve.emit(true);
    }

    onRejected(): void {
        this.reject.emit(true);
    }

    closeModal(): void {
        $(`#${this.modal.modalId}`).modal('hide');
    }
    showModal():void{
        $(`#${this.modal.modalId}`).modal('show');
    }

}

export enum MODAL_EVENT{
    ONSHOW = 'ONSHOW'
}
