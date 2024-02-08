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

  searchText!: string;
  categorySearch: string = "";

  menuCategoryItems: any[] = [];

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
    this.data.getInternalizationFromServerWithLanguage(this.user.language).then(r => {
      this.internalization = JSON.parse(localStorage.getItem('internalization') || '{}');
    });
  }

  addNameToSearch() {
    if (this.searchText.trim() !== '') {
      this.route.queryParams.subscribe(params => {
        const updatedParams = {...params, name: this.searchText};
        this.router.navigate(['/search'], {queryParams: updatedParams}).then(() => {
          window.location.reload();
        });
      });
    } else {
      window.location.href = '/search?name=' + this.searchText;
    }
  }


  addCategoryToSearch(category: any) {
    this.route.queryParams.subscribe(params => {
      const updatedParams = {...params, category: category};
      this.categorySearch = category;
      this.router.navigate(['/search'], {queryParams: updatedParams}).then(r => {
        window.location.reload()
      });
    });
  }


}
