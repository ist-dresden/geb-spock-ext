package test.example

import test.framework.Spec

/**
 * This examples needs an appropriate configuration:
 * <ul>
 *     <li>a running CQ instance,</li>
 *     <li>a target configuration which points to the running CQ instance,</li>
 *     <li>optional a concrete a driver configuration.</li>
 * </ul>
 * See TargetConfig examples 'target=local_cq' or 'target=back_to_local_cq'.
 */
class CQExample extends Spec {

    /**
     * test open siteadmin in CQ for a well known path
     */
    def 'geometrixx in siteadmin'() {

        setup:
        reportGroup "siteadmin" // folder for this test and its screenshots
        setBaseUrl('/')         // use site root for going to 'siteadmin

        when:
        go 'siteadmin#/content/geometrixx'
        // wait for 4 seconds to give the siteadmin a chance
        // to display the normal user interface
        wait(4)

        then:
        // make a screenshot and compare the stored image
        // with the corresponding template
        screenshot().save().validate()
    }

    /**
     * test open 'Geometrixx Homepage' and a running carousel is visible
     */
    def 'geometrixx home'() {

        setup:
        reportGroup "geometrixx home"   // folder for this test and its screenshots
        // the baseURL of the target configuration is used by default for each test

        when:
        go 'en.html'
        waitFor(10) {   // wait up to 10 seconds for the carousel component
            $('div.cq-carousel', 0).displayed
        }

        then:
        // make a screenshot,
        // clear the carousel area (dynamic area - not useful for comparing),
        // shrink scrteenshot to the main area of the page,
        // save image and compare with the template (if present)
        screenshot().hide($('div.cq-carousel', 0)).crop($('#main_bg')).save().validate()
    }

    /**
     * test open 'Geometrixx Products Page' and right sidebar with image is visible
     */
    def 'geometrixx content'() {

        setup:
        reportGroup "geometrixx products"
        // set window size to define the width for rendering and the screenshot images
        window.size(1000, 600)

        when:
        go 'en/products.html'
        waitFor(10) {
            $('#_content_geometrixx_en_jcr_content_rightpar_teaser .cq-dd-image').displayed
        }

        then:
        screenshot().save().validate()
    }

    /**
     * test open a set of pages
     */
    def 'geometrixx page set'() {

        setup:
        reportGroup "geometrixx page set"
        window.size(1000, 600)

        when:
        // use test parameter (declared in the 'where' block)
        go "${page}"

        then:
        // save a screenshot for each page using the page path as screenshot filename
        screenshot().save("${page}").validate()

        where:
        // run this test in a loop with the following loop parameters
        page << [
                'en/products/triangle.html',
                'en/products/square.html',
                'en/products/circle.html'
        ]
    }
}
