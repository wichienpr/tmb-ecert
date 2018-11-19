import { Directive, Input, OnInit, OnDestroy, ElementRef, HostListener, Renderer2 } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AjaxService, CommonService } from '../../services';
import { DatatableDirective } from './datatable.directive';

@Directive({
  selector: '[ngSort]',
  // exportAs: "ngSort"
})
export class NgSortDirective implements OnInit {

  @Input("ngSort")
  mainDt: DatatableDirective;

  @Input("columnName")
  columnName: string;

  private sortType: SORT_TYPE = SORT_TYPE.ASC;
  private ASC_CSS: string[] = ["sorted", "ascending"];
  private DESC_CSS: string[] = ["sorted", "descending"];
  private SORT_HEADER_CSS: string[] = ["sorted", "ngsortth"];
  private sortId: string = null;
  private clearFlag: boolean = false;

  constructor(private _ngEl: ElementRef, private _renderer: Renderer2) {
  }

  ngOnInit() {
    this.sortId = "U" + Math.random().toString(36).substr(2, 9);
    // console.log("SortId : ", this.sortId);
    this.mainDt.sortOutput.subscribe((e: SortEvent) => {
      // console.log("ngSort ==>", e);
      if (e.sortId === this.sortId) {
        //skip
      } else {
        this._clear();
      }
    });
    //clear
    this._clear();
  }

  @HostListener('click') onClick() {
    // console.log("click");

    if (this.clearFlag) {
      this.clearFlag = false;
      this._removeClass(this.SORT_HEADER_CSS);
      this._removeClass(this.DESC_CSS);
      this._addClass(this.ASC_CSS);
      this.sortType = SORT_TYPE.ASC;

    } else if (this.sortType == SORT_TYPE.ASC) {
      this._removeClass(this.SORT_HEADER_CSS);
      this._removeClass(this.ASC_CSS);
      this._addClass(this.DESC_CSS);
      this.sortType = SORT_TYPE.DESC;
    } else {
      this._removeClass(this.SORT_HEADER_CSS);
      this._removeClass(this.DESC_CSS);
      this._addClass(this.ASC_CSS);
      this.sortType = SORT_TYPE.ASC;
    }

    let e: SortEvent = {
      sortId: this.sortId,
      sortType: this.sortType,
      column: this.columnName
    };
    this.mainDt.sortOutput.emit(e);

  }

  private _removeClass(pcls: string[]) {
    pcls.forEach(klass => {
      this._renderer.removeClass(this._ngEl.nativeElement, klass);
    });
  }


  private _addClass(pcls: string[]) {
    pcls.forEach(klass => {
      this._renderer.addClass(this._ngEl.nativeElement, klass);
    });
  }

  private _clear() {
    if(this.clearFlag){
      return;
    }
    this._removeClass(this.ASC_CSS);
    this._removeClass(this.DESC_CSS);
    this._addClass(this.SORT_HEADER_CSS);
    this.clearFlag = true;
  }

}

enum SORT_TYPE {
  ASC = "ASC",
  DESC = "DESC"
}

interface SortEvent {
  sortId: string,
  sortType: SORT_TYPE,
  column?: string
}