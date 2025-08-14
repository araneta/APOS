import { Component, EventEmitter, HostListener, input, Input, OnInit, Output } from "@angular/core";
import { AccountManagementService } from "../../api/api/accountManagement.service";
import { CommonModule } from "@angular/common";
import { Account } from "../../api";

export interface ParentAccountItem{
    code:string;
    name:string;
}
@Component({
    selector:'account-parent-dropdown',
    templateUrl:'./accoount-parent-dropdown.component.html',
    imports:[CommonModule]
})
export class AccountParentDropdownComponent implements OnInit{
    items:ParentAccountItem[]= [{
        code:"item1",
        name:"item11"
    },{
        code:"item2",
        name:"item22"
    }];
    isOpen = false;
    selectedItemLabel = "select item";

    @Output() itemSelected = new EventEmitter<ParentAccountItem>();
    @Input() defaultSelectedItem: ParentAccountItem|null = null;
    @Input() categoryID: string = "";

    constructor(private accountManagementService: AccountManagementService,){

    }

    ngOnInit(): void {
        this.accountManagementService.findAllParentsByAccountCategory(this.categoryID).subscribe(
            (response: Account[]) => {
                console.log('API Response:', response);
                // Optionally map response to ParentAccountItem[] if needed
                this.items = response.map(acc => ({
                    code: acc.code ?? "",
                    name: acc.name ?? ""
                }));
            },
            (error) => {
                console.error('Error details:', {
                  status: error.status,
                  statusText: error.statusText,
                  error: error.error,
                  message: error.message
                });                
            }
        );
    }

    toggleDropdown():void{
        this.isOpen = !this.isOpen;
    }

    selectItem(item:ParentAccountItem):void{
        this.selectedItemLabel = `${item.code} - ${item.name}`;
        this.itemSelected.emit(item);
        this.isOpen = false;
    }
    @HostListener('document:click', ['$event'])
    onClickOutside(event:Event){
        const target = event.target as HTMLElement;
        if(!target.closest('.dropdown')){
            this.isOpen = false;
        }
    }
}