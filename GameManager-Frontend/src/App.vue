<template>
  <div id="app">
    <!-- Include the Navigation Bar -->
    <NavigationBar />

    <!-- The main content of the app will be displayed here -->
    <router-view />
  </div>
</template>

<script>
import NavigationBar from "./components/NavigationBar.vue";

export default {
  name: "App",
  components: {
    NavigationBar, // Register the navigation bar component
  },
};
</script>

<style>
/* Global styles for the application */
body {
  margin: 0;
  font-family: Arial, sans-serif;
}

#app {
  display: flex;
  flex-direction: column;
  min-height: 0vh;
}
</style>