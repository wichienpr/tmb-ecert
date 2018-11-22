import { Directive, Input, OnInit, OnDestroy, Output, EventEmitter } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AjaxService, CommonService } from '../../services';

@Directive({
  selector: '[ngDatatable]',
  exportAs: "ngDatatable"
})
export class DatatableDirective implements OnInit , OnDestroy {

  @Input("ngDatatable") config: DatatableCofnig;
  @Output("sortOutput") sortOutput: EventEmitter<any> = new EventEmitter();

  resp: DatatableResp = {
    data: [],
    currentPageData: [],
    recordsTotal: 0
  };
  sortColum: DatatableSort[] = [];
  req: any = {};
  isSearchFlag: boolean = false;
  pagging: DatatablePagging = {
    show: 0,
    in: 0,
    of: 0,
    pageLength: [10, 20, 50, 100],
    currentLength: 10,
    totalPage: 0,
    page: 1,
    buttons: [],
    buttonRange: 5
  }

  constructor(private httpclient: HttpClient, private commonsvr: CommonService) {
  }

  ngOnInit(): void {
    // console.log("init ngDatatable", this.config);
    if (this.config.sortColum) {
      this.sortColum = Object.assign({}, this.config.sortColum)
    }

    this.sortOutput.subscribe ( item=>{
      // console.log("ngDatatable ==>", item);

        if(item.column){
          this.sortColum = [{
            column : item.column,
            order : item.sortType
          }];
          this.search();
        }else{
          // console.log(" columnName=\"EXAMPLE\" <=== is not defind in [th]  ");
        }
    });
  }

  reload(): Promise<string> {
    return new Promise((resolve, reject) => {

      if (this.config.useBlockUi) {
        this.commonsvr.blockui();
      }

      if (!this.config.url) {
        reject("URL : is not defind");
        if (this.config.useBlockUi) {
          this.commonsvr.unblockui();
        }
        throw new Error('URL : is not defind');
      }

      let params = Object.assign(this.req, {
        sort: this.sortColum
      });

      if (this.config.serverSide) {
        console.log("PAGING", this.pagging);
        let start = (this.pagging.page - 1) * this.pagging.currentLength;
        let plength = this.pagging.currentLength;
        params = Object.assign(params, {
          "start": start,
          "length": plength
        });
      }


      // console.log("parmas : ", params);
      this.isSearchFlag = true;

      this.httpclient.post(AjaxService.CONTEXT_PATH + this.config.url, params).subscribe(
        (resp: DatatableResp) => {
          this.resp = resp;
          if (!this.config.serverSide) {
            this.pagesplite(this.resp.data, 1);
          } else {
            this.pageServerside(this.resp.data, this.pagging.page);
          }
          // console.log(this.resp)
          resolve("OK");
          if (this.config.useBlockUi) {
            this.commonsvr.unblockui();
          }
        },
        error => {
          reject("error");
          if (this.config.useBlockUi) {
            this.commonsvr.unblockui();
          }
          throw new Error("call Datatable error")
        }
      )

    });

  }

  chagePage(pageLength: string) {
    // console.log("change", pageLength);
    this.pagging.currentLength = parseInt(pageLength);
    this.pagging.page = 1;
    if (!this.isSearch) {
      return false;
    }

    if (!this.config.serverSide) {
      this.pagesplite(this.resp.data, this.pagging.page);
    } else {
      //server side
      this.reload();
    }

  }

  private pagesplite(data: any[], pageNumber: number = 1) {
    let totalPage = Math.ceil(data.length / this.pagging.currentLength);
    // console.log("totalPage", totalPage);
    this.pagging.totalPage = totalPage;
    this.pagging.page = pageNumber;
    let offset = (pageNumber - 1) * this.pagging.currentLength + 1;
    this.pagging.in = offset;
    let start = (pageNumber - 1) * this.pagging.currentLength;
    let end = start + this.pagging.currentLength;

    this.pagging.show = offset;
    this.pagging.in = ((offset + this.pagging.currentLength) - 1) > data.length ? data.length : (offset + this.pagging.currentLength) - 1;
    this.pagging.of = data.length;

    // console.log(start, end, this.pagging);
    this.resp.currentPageData = data.slice(start, end);

    this.calcbuttons();

  }

  private pageServerside(data: any[], pageNumber: number = 1) {
    let totalRecrod = this.resp.recordsTotal;
    let totalPage = Math.ceil(totalRecrod / this.pagging.currentLength);
    // console.log("totalPage", totalPage);
    this.pagging.totalPage = totalPage;
    this.pagging.page = pageNumber;
    let offset = (pageNumber - 1) * this.pagging.currentLength + 1;
    this.pagging.in = offset;
    let start = (pageNumber - 1) * this.pagging.currentLength;
    let end = start + this.pagging.currentLength;

    this.pagging.show = offset;
    this.pagging.in = ((offset + this.pagging.currentLength) - 1) > totalRecrod ? totalRecrod : (offset + this.pagging.currentLength) - 1;
    this.pagging.of = totalRecrod;

    // console.log(start, end, this.pagging);
    this.resp.currentPageData = data;
    this.resp.data = data;

    this.calcbuttons();

  }

  private calcbuttons() {
    let totalpage = this.pagging.totalPage;
    let buttons = [];
    if (totalpage <= this.pagging.buttonRange) {
      for (let index = 1; index <= totalpage; index++) {
        buttons.push(index);
      }
    } else {
      let brange = this.pagging.buttonRange;
      let mid = Math.ceil(brange / 2);
      let p = this.pagging.page;
      let t = this.pagging.totalPage;
      if (p < mid) {
        for (let index = 1; index <= brange; index++) {
          buttons.push(index);
        }
      } else {
        for (let index = p - 2; index < p; index++) {
          buttons.push(index);
        }

        let max = (p + 2 > t) ? t : p + 2;
        for (let index = p; index <= max; index++) {
          buttons.push(index);
        }
      }
    }

    this.pagging.buttons = buttons;

  }

  nextPage(page: number) {
    // console.log("page ", page);
    if (page < 1) {
      throw new Error("page > 1");
    }
    if (page > this.pagging.totalPage) {
      throw new Error("page < max length");
    }

    if (!this.config.serverSide) {
      this.pagesplite(this.resp.data, page);
    } else {
      //server side
      this.pagging.page = page;
      // console.log("nextPage", page);
      this.reload();
    }
  }

  back() {
    let p = this.pagging.page - 1;
    if (p >= 1) {
      this.nextPage(p);
      this.pagging.page = p;
    }
  }

  next() {
    let p = this.pagging.page + 1;
    if (p <= this.pagging.totalPage) {
      this.nextPage(p);
      this.pagging.page = p;
    }
  }

  sort(column: string): Promise<string> {
    this.pagging.page = 1;
    let columnstr = column.trim();
    let isfind: boolean = false;
    let newSort: DatatableSort[] = [];
    for (let index = 0; index < this.sortColum.length; index++) {
      const element = this.sortColum[index];
      if (columnstr == element.column) {
        if (element.order == DatatableOrder.ASC) {
          element.order = DatatableOrder.DESC
        } else {
          element.order = DatatableOrder.ASC
        }
        newSort.push(element);
        isfind = true;
        break;
      }
    }

    if (!isfind) {
      newSort.push({ column: columnstr, order: DatatableOrder.ASC });
    }

    this.sortColum = newSort;

    // console.log("this.sortColum", this.sortColum);
    //reload
    return this.reload();
  }

  searchParams(p: any) {
    this.req = Object.assign({}, p);
  }

  search(): Promise<string> {
    this.pagging.page = 1;
    return this.reload();
  }

  clear() {
    this.isSearchFlag = false;
    this.pagging.page = 1;
    this.resp = {
      data: [],
      currentPageData: [],
      recordsTotal: 0
    }
  }

  ngOnDestroy(): void {
    this.clear();
  }

  get isEmpty() {
    return this.resp.data.length === 0;
  }

  get isSearch() {
    return this.isSearchFlag;
  }

  get datas() {
    return this.resp.currentPageData;
  }

  get intshow() {
    return this.pagging.show;
  }
  get intin() {
    return this.pagging.in;
  }
  get intof() {
    return this.pagging.of;
  }
  get rownum() {
    return this.pagging.currentLength * (this.pagging.page - 1);
  }

  get pageLength() {
    return this.pagging.pageLength;
  }

  get page() {
    return this.pagging.page;
  }

  get totalpage() {
    return this.pagging.totalPage;
  }

  get buttons() {
    return this.pagging.buttons;
  }

}


export interface DatatableCofnig {
  url: string;
  serverSide?: boolean;
  useBlockUi?: boolean;
  sortColum?: DatatableSort[];
}

interface DatatableSort {
  column: string;
  order?: string;//asc desc
}

enum DatatableOrder {
  ASC = "asc",
  DESC = "desc"
}

interface DatatableResp {
  data: any[];
  recordsTotal: number;
  currentPageData: any[];
}

interface DatatablePagging {
  show: number,
  in: number,
  of: number,
  pageLength: number[],
  currentLength: number,
  totalPage: number,
  page: number,
  buttons: number[],
  buttonRange: number
}
