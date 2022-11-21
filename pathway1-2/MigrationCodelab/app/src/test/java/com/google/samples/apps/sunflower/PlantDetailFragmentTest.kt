@RunWith(AndroidJUnit4::class)
class PlantDetailFragmentTest {

    @Rule
    @JvmField
    val composeTestRule = createAndroidComposeRule<GardenActivity>()

    ...

    @Before
    fun jumpToPlantDetailFragment() {
        populateDatabase()

        composeTestRule.activityRule.scenario.onActivity { gardenActivity ->
            activity = gardenActivity

            val bundle = Bundle().apply { putString("plantId", "malus-pumila") }
            findNavController(activity, R.id.nav_host).navigate(R.id.plant_detail_fragment, bundle)
        }
    }

    @Test
    fun testPlantName() {
        composeTestRule.onNodeWithText("Apple").assertIsDisplayed()
    }

}