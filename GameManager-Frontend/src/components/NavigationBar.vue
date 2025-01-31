<template>
  <div class="app" id="app">
    <nav class="rounded-navbar">
      <ul class="nav-menu" id="navMenu">
        <li
            v-for="(item, index) in menuItems"
            :key="index"
            :class="{ active: selectedItem === item }"
            @click="selectItem(item, index)"
            class="nav-item"
        >
          <a href="javascript:void(0);">{{ item }}</a>
        </li>
      </ul>
    </nav>
    <div class="extra-line">
      Website is still in development. Contents are not representative of the website’s final quality.
    </div>
  </div>
</template>

<script>
export default {
  name: "NavigationBar",
  data() {
    return {
      menuItems: ["Home", "Search", "Log in"], // Default menu items
      menuItemRef: ["/", "/search", "/login"], // Default menu links
      selectedItem: "Home", // Default selected item
    };
  },
  mounted() {
    this.updateMenuItems(); // Update menu items dynamically based on session storage
  },
  methods: {
    // Update the selected item and navigate
    selectItem(item, index) {
      this.selectedItem = item;
      this.$router.push(this.menuItemRef[index]); // Navigate to the corresponding route
    },
    // Update menu items dynamically based on session storage
    updateMenuItems() {
      const isOwner = sessionStorage.getItem("owner") !== null;
      const isEmployee = sessionStorage.getItem("employee") !== null;
      const isCustomer = sessionStorage.getItem("customer") !== null;
      console.log("isOwner",isOwner);
      console.log("isEmployee",isEmployee);
      console.log("isCustomer",isCustomer);
      // TODO: design different homePage for staff and customer so that edit game is disable for customers.
      if (isOwner) {
        this.menuItems = [
          "Home",
          "Create Game",
          "Requests",
          "Create Category",
          "Category List",
          "Employees",
          "Search",
          "Store Information",
          "Profile",
        ];
        this.menuItemRef = [
          "/",
          "/create-game",
          "/approve-games",
          "/create-category",
          "/category-list",
          "/employees",
          "/search",
          "/store-information",
          "/profile",
        ];
      } else if (isEmployee) {
        this.menuItems = ["Home", "Create Game", "Customer", "Search", "Profile"];
        this.menuItemRef = [
          "/",
          "/create-game",
          "/customer",
          "/search",
          "/profile",
        ];
      } else if (isCustomer) {
        this.menuItems = [
          "Home",
          "Search",
          "Orders",
          "Cart",
          "Wish List",
          "Profile",
        ];
        this.menuItemRef = [
          "/",
          "/search",
          "/orders",
          "/cart",
          "/wish-list",
          "/profile",
        ];
      }
    },
  },
};
</script>

<style>
/* General reset */
body,
html {
  margin: 0;
  padding: 0;
  font-family: Arial, sans-serif;
}

.app {
  display: flex;
  flex-direction: column;
}

/* Navigation bar container */
.rounded-navbar {
  position: fixed;
  top: 0;
  left: 250px; /* Align to the left */
  z-index: 1000; /* Ensures it appears above other content */
  width: 70%;
  display: flex;
  justify-content: center;
  background-color: #f8f9fa; /* Light gray background */
  border-radius: 50px; /* Fully rounded edges */
  padding: 10px 20px; /* Space around the menu items */
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* Subtle shadow */
  z-index: 1000;
}

/* Navigation menu styling */
.nav-menu {
  list-style: none; /* Remove bullets */
  display: flex; /* Align items in a row */
  gap: 20px; /* Space between links */
  margin: 0;
  padding: 0;
}

/* Extra line for development notice */
.extra-line {
  position: fixed; /* Fixed at the bottom of the viewport */
  bottom: 0;
  left: 0;
  width: 100%;
  text-align: center;
  padding: 10px 0;
  background-color: #f8f9fa; /* Light background */
  color: #555;
  font-size: 16px;
  box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1); /* Optional shadow */
}

/* Navigation items */
.nav-item a {
  text-decoration: none; /* Remove underline */
  color: #333; /* Dark text color */
  font-size: 16px;
  font-weight: bold;
  padding: 5px 10px;
  transition: color 0.3s ease;
}

/* Hover effect */
.nav-item a:hover {
  color: #007bff; /* Blue color on hover */
}

/* Active state with a dot indicator */
.nav-item.active a {
  position: relative;
  color: #000; /* Black for the active link */
}

.nav-item.active a::before {
  content: "•"; /* Dot before the active link */
  color: #000;
  position: absolute;
  left: -10px; /* Adjust the position of the dot */
  font-size: 20px;
}

@media (max-width: 600px) {
  .nav-menu {
    flex-direction: column;
    align-items: center;
    gap: 10px; /* Reduce gap between items */
  }
}

.nav-item a:hover {
  background-color: #e9ecef; /* Light gray background */
  border-radius: 5px;
}

.nav-item.active a::before {
  transition: transform 0.3s ease;
  transform: scale(1.2); /* Slightly enlarges the dot */
}
</style>