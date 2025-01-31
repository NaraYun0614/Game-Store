import { createRouter, createWebHistory } from 'vue-router';
import GameList from '../components/GameList.vue';
import GameDetails from '../components/GameDetails.vue';
import CreateGame from '../components/CreateGame.vue';
import UpdateGame from '../components/UpdateGame.vue';
import Login from '@/components/Login.vue';
import ApproveGames from "@/components/ApproveGames.vue";
import Cart from "@/components/Cart.vue";
import Payment from "@/components/Payment.vue";
import CreateCategory from "@/components/CreateCategory.vue";
import CategoryList from '../components/CategoryList.vue';
import CategoryDetails from '../components/CategoryDetails.vue';
import UpdateCategory from '../components/UpdateCategory.vue';
import AddRemoveCategories from "@/components/AddRemoveCategories.vue";
import CreateAccount from "@/components/CreateAccountForm.vue";
import AddRemoveGames from "@/components/AddRemoveGames.vue";
import SearchGames from "@/components/SearchGames.vue";
import Profile from "@/components/Profile.vue";
import Orders from "@/components/Orders.vue";
import Wishlist from "@/components/Wishlist.vue";

const routes = [
  { path: '/approve-games',
    component: ApproveGames
  },

  {
    path: '/',
    name: 'GameList',
    component: GameList,
  },
  {
      path: '/category-list',
      name: 'CategoryList',
      component: CategoryList,
    },
   {
       path: '/categories/:id',
       name: 'CategoryDetails',
       component: CategoryDetails,
       props: true,
     },
  {
    path: '/games/:id',
    name: 'GameDetails',
    component: GameDetails,
    props: true,
  },
  {
    path: '/create-game',
    name: 'CreateGame',
    component: CreateGame,
  },
  {
    path: '/update-game/:id',
    name: 'UpdateGame',
    component: UpdateGame,
    props: true,
  },

  {
    path: '/cart',
    name: 'Cart',
    component: Cart,
  },
  {
    path: '/payment',
    name: 'Payment',
    component: Payment,
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile,
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/',
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },

  {
    path: '/create-game',
    name: 'CreateGame',
    component: CreateGame,
  },
  {
    path: '/',
    redirect: '/login',
  },
  {
    path: '/create-category',
    name: 'CreateCategory',
    component: CreateCategory,
  },
  {
      path: '/update-category/:id',
      name: 'UpdateCategory',
      component: UpdateCategory,
      props: true,
    },
   {
       path: "/categories/manage",
       name: "AddRemoveCategories",
       component: AddRemoveCategories,
     },
  {
    path: "/games/manage",
    name: "AddRemoveGames",
    component: AddRemoveGames,
  },
  {
    path: '/games/update/:id',
    name: 'UpdateGame',
    component: UpdateGame,
    props: true,
  },
  {
    path: '/search',
    name: 'SearchGames',
    component: SearchGames
  },
  {
      path: '/orders',
      name: 'Orders',
      component: Orders
    },
  {
        path: '/wishlist',
        name: 'Wishlist',
        component: Wishlist
      },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;