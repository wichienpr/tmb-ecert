import { Component, OnInit } from '@angular/core';
declare var $: any;
@Component({
  selector: 'app-role-management',
  templateUrl: './role-management.component.html',
  styleUrls: ['./role-management.component.css']
})
export class RoleManagementComponent implements OnInit {
  showData: boolean = false;  

  action: string[];
  selectaction: string;

   modal: string[] = ['uploadfile'];
  constructor() { 
     this.action = [
    
      "All",
      "Active",
      "Inactive"
     
  ];
  ;

}
  ngOnInit() {   

  }


  ngAfterViewInit() {

  }
  searchData(): void {
    this.showData = true;
    setTimeout(() => {
      $("#table").DataTable({
        scrollX: true,
        searching: false,
        orderable : false,             
        "columnDefs": [{
          "targets": [3],
          "orderable": false
        }]
      });
    });

  }

  clearData(): void {
    this.showData = false;
  }

  Onselectaction = event => {
    this.selectaction = this.action[event.target.value];
  };
  
  openModal(id) {
    $(`#${id}`).modal('show');
  }

  closeModal(id) {
    $(`#${id}`).modal('hide');
  }

}
