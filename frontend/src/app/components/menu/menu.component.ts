import {Component, Input, OnInit} from '@angular/core';
import {faMagnifyingGlass, faSearch, faXmark} from '@fortawesome/free-solid-svg-icons';
import {DataService} from "../../data.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-menu',
  templateUrl: '../menu/menu.component.html',
  styleUrls: ['../menu/menu.component.css']
})
export class MenuComponent implements OnInit {
  @Input() pageTitle!: string;
  @Input() logoSrc!: string;

  faMagnifyingGlass = faMagnifyingGlass
  faXmark = faXmark
  faSearch = faSearch

  user: any = {};
  internalization: any = {};
  categories: any = {};
  showFullscreenSearch: boolean = false;

  searchText: string = "";
  categorySearch: string = "";

  constructor(private route: ActivatedRoute,
              private router: Router,
              private data: DataService) {

  }


  toggleFullscreenSearch(state: boolean) {
    this.showFullscreenSearch = state;
  }

  ngOnInit(): void {
    this.user = this.data.getUser();
    this.categories = this.data.getCategories();
  }

  addNameToSearch() {
    this.route.queryParams.subscribe(params => {
      const updatedParams = {...params, name: this.searchText};
      this.router.navigate(['/search'], {queryParams: updatedParams}).then(r => {
        window.location.reload()
      });
    });
  }


  addCategoryToSearch(category: any) {
    this.route.queryParams.subscribe(params => {
      const updatedParams = {...params, category: category};
      this.router.navigate(['/search'], {queryParams: updatedParams}).then(r => {
        window.location.reload()
      });
    });
  }

}
