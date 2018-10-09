import { Component, OnInit } from '@angular/core';
import { Srn01000Service } from 'app/tmb-ecert/srn00000/srn01000/srn01000.service';
import { FormGroup } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AjaxService } from 'app/baiwa/common/services';
import { isValid } from 'app/baiwa/common/helpers';


@Component({
  selector: 'app-srn01000',
  templateUrl: './srn01000.component.html',
  styleUrls: ['./srn01000.component.css'],
  providers: [Srn01000Service]
})
export class Srn01000Component implements OnInit {
  form: FormGroup;
  statusHomePage: string;
  status: string;
  dataT: any[] = [];
  showData: boolean = false;
  loading: boolean = false;

  constructor(private srn01000Service: Srn01000Service,
    private ajax: AjaxService,
    private router: Router,
    private route: ActivatedRoute) {

    this.srn01000Service.getForm().subscribe(form => {
      this.form = form
    });

  }

  ngOnInit() {
    this.dataT = [];
    this.statusHomePage = this.route.snapshot.queryParams["codeStatus"];

    if (this.statusHomePage) {
      this.searchStatusByHomePage(this.statusHomePage)
    } 

  }


  getDataByTmbReqNo = () => {
    console.log(this.form);
    this.loading = true;
    const URL = "/api/srn/srn01000/findReqByTmbReqNo";
    this.ajax.post(URL, {
      tmbReqNo: this.form.controls.tmbReqNo.value,
    }, async res => {
      const data = await res.json();
      setTimeout(() => {
        this.loading = false;
      }, 50);
      data.forEach(element => {
        this.dataT.push(element);
      });
      
    });
  }

  getDataByStatus(code) {
    this.status = code;
    this.loading = true;
    const URL = "/api/srn/srn01000/findReqByStatus";
    this.ajax.post(URL, { status: this.status }, res => {
      //console.log(res.json());
      setTimeout(() => {
        this.loading = false;
      }, 1);
      res.json().forEach(element => {
        this.dataT.push(element);
      });
    });

  }

  searchData(): void {
    console.log(this.form.controls['tmbReqNo'].value);
    console.log("searchData");
    if(this.form.controls['tmbReqNo'].value == "" || this.form.controls['tmbReqNo'].value == null){
      this.dataT = [];
    }else{
      this.showData = true;
      this.getDataByTmbReqNo();
      this.dataT = [];
    }
    
  }

  searchStatusByHomePage(code): void {
    this.showData = true;
    this.getDataByStatus(code);
    this.dataT = [];
  }

  clearData(): void {
    console.log("clearData");
    this.form.reset();
    this.showData = false;
    this.dataT = [];
  }



  getFontStyeColor(status) {
    if (status == '10001' || status == '10005' || status == '10009' || status == '10011') {
      return '#2185D0';
    } else if (status == '10002' || status == '10007' || status == '10010') {
      return 'gray';
    } else if (status == '10003' || status == '10004' || status == '10005' || status == '10006') {
      return 'red';
    }

  }


  getButtonStyeColor(status) {
    if (status == '10001' || status == '10005' || status == '10009' || status == '10011') {
      return 'ui blue basic button center';
    } else if (status == '10002' || status == '10007' || status == '10010') {
      return 'ui gray basic button center';
    } else if (status == '10003' || status == '10004' || status == '10005' || status == '10006') {
      return 'ui red basic button center';
    }

  }

  validate(input: string, submitted: boolean) {
    return isValid(this.form, input, submitted);
  }

}
