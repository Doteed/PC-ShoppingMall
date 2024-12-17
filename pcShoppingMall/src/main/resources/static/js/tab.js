/**
 * 
 */
<script>
		let key = "${param.key}";
		console.log(key);
		if (key === "write-myreviews") {
			$("#written-myreviews-tab").removeClass("active");
			$("#write-myreviews-tab").addClass("active");

			$("#written-myreviews").removeClass("show active");
			$("#write-myreviews-tab").addClass("show active");
		} else if (key === "written-myreviews") {
			$("#write-myreviews-tab").removeClass("active");
			$("#written-myreviews-tab").addClass("active");

			$("#write-myreviews").removeClass("show active");
			$("#written-myreviews-tab").addClass("show active");
		}
</script>