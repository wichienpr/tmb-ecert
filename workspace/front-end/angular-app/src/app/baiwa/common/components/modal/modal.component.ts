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
    styleUrls: ["./modal.component.css"]
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

    constructor() {
        this.approve = new EventEmitter<boolean>();
        this.reject = new EventEmitter<boolean>();
    }

    ngAfterViewInit() {
        // On initial modal
        $(`#${this.modal.modalId}`).modal({ closable: false, centered: false, autofocus: false });
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

}