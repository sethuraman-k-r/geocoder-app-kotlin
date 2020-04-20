package com.learn.kotlin

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.JavaScript
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.PWA
import com.vaadin.flow.shared.ui.LoadMode
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.apache.tomcat.jni.Lock.name
import com.vaadin.flow.component.page.Page



/**
 * A sample Vaadin view class.
 *
 *
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 *
 *
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@Route
@PWA(name = "Geocoder Application", shortName = "Geocode App", description = "Geocode App from Kotlin, Vaadin & Spring Boot",
        enableInstallPrompt = false)
class MainView
(@Autowired dataService: DataService) : VerticalLayout() {

    init {

        var horizontalLayout = HorizontalLayout()
        horizontalLayout.width = "100%"

        var mapDiv = Div()
        mapDiv.width = "50%"
        mapDiv.setId("mapBox")

        var leftVerticalLayout = VerticalLayout()
        var searchLayout = HorizontalLayout()
        val searchField = TextField()
        var grid: Grid<GeoInfo> = Grid(GeoInfo::class.java);
        grid.setColumns("category", "value")
        val button = Button("Search", VaadinIcon.SEARCH.create()) { _ ->
            run {

                val result = dataService.getPlaceData(searchField.value)
                if (result.isNullOrEmpty()) {
                    println("Something went wrong")
                } else {
                    var jsonResult = JSONObject(result);
                    var results: JSONArray = jsonResult.getJSONArray("results")
                    if(results.length() > 0) {
                        var result: JSONObject = results.getJSONObject(0)
                        var geoInfos: List<GeoInfo> = listOf(
                                GeoInfo("Continent", result.getJSONObject("components").optString("continent", "-")),
                                GeoInfo("Country", result.getJSONObject("components").optString("country", "-")),
                                GeoInfo("Country Code", result.getJSONObject("components").optString("country_code","-")),
                                GeoInfo("State", result.getJSONObject("components").optString("state", "-")),
                                GeoInfo("State Code", result.getJSONObject("components").optString("state_code", "-")),
                                GeoInfo("City", result.getJSONObject("components").optString("city", "-")),
                                GeoInfo("Coordinates", "${result.getJSONObject("geometry").get("lat")} ${result.getJSONObject("geometry").get("lng")}"),
//                                GeoInfo("Calling Code", "${result.getJSONObject("annotations").getJSONObject("UN_M49").get("callingcode")}"),
                                GeoInfo("Currency Name", result.getJSONObject("annotations").getJSONObject("currency").optString("name", "-")),
                                GeoInfo("Currency Code",result.getJSONObject("annotations").getJSONObject("currency").optString("iso_code", "-")),
                                GeoInfo("Currency Subunit", result.getJSONObject("annotations").getJSONObject("currency").optString("subunit", "-")),
                                GeoInfo("Timezone", result.getJSONObject("annotations").getJSONObject("timezone").optString("name", "-")),
                                GeoInfo("Timezone Offset", result.getJSONObject("annotations").getJSONObject("timezone").optString("offset_string", "-")),
                                GeoInfo("Timezode Code", result.getJSONObject("annotations").getJSONObject("timezone").optString("short_name", "-"))
                        )
                        grid.setItems(geoInfos)
                        drawMap(arrayOf(result.getJSONObject("geometry").getDouble("lat"),
                                result.getJSONObject("geometry").getDouble("lng")))
                    }
                }
            }
        }
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY)

        searchLayout.width = "50%"
        leftVerticalLayout.width = "50%"
        searchField.placeholder = "Search place here"
        searchLayout.add(searchField, button)
        leftVerticalLayout.add(searchLayout, grid)

        horizontalLayout.add(leftVerticalLayout, mapDiv)

        UI.getCurrent().page.addStyleSheet("https://unpkg.com/leaflet@1.6.0/dist/leaflet.css")
        UI.getCurrent().page.addJavaScript("https://unpkg.com/leaflet@1.6.0/dist/leaflet.js")

        add(horizontalLayout)
    }

    fun drawMap(coords: Array<Double>) {
        val page = UI.getCurrent().page
        page.executeJs(
                """
                                var container = L.DomUtil.get('mapBox');
                                if(container != null){
                                  container._leaflet_id = null;
                                }
                                var map = L.map('mapBox').setView([$0, $1], 13);
                                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                                    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                                    }).addTo(map);
                                L.marker([$0, $1]).addTo(map);
                            """,
                coords[0], coords[1])
    }

}
