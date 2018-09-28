import { Component, OnInit } from '@angular/core';
import { AjaxService } from 'app/baiwa/common/services/ajax.service';
import { forEach } from '@angular/router/src/utils/collection';
declare var $: any;
const URL = {
  export:"rep/rep01000/exportFile"
}
@Component({
  selector: 'app-rep01000',
  templateUrl: './rep01000.component.html',
  styleUrls: ['./rep01000.component.css']
})
export class Rep01000Component implements OnInit {
  products: String[];  
  showData: boolean = false; 
  selectProduct: String;
  dataT: any[]= [];
  loading: boolean = false;
  

  constructor(
    private ajax: AjaxService,
  ) { 
   
  }

  ngOnInit() {}

  ngAfterViewInit() {}

  onSelectProducts = event => {
    this.selectProduct = this.products[event.target.value];
  };
  
  getData=()=>{
    this.dataT=[];
    const URL = "rep/rep01000/list";
    this.ajax.post(URL,{},async res => {
      const data = await res.json();
      data.forEach(element => {
        this.dataT.push(element);
      });
    console.log("getData True : Data s",this.dataT);
    });
  }

  searchData(): void {
    console.log("searchData");
    this.showData = true;
    this.getData();
  }
  
  clearData(): void {
    console.log("clearData");
    this.showData = false;
  }

  exportFile=()=>{
    console.log("exportFile");
    this.ajax.download(URL.export);
  }

 
}
