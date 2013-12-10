package test.framework

import geb.Browser
import geb.navigator.Navigator
import geb.report.ScreenshotReporter
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebElement
import test.framework.image.ScreenImage
import test.framework.report.Reporting

import java.awt.Color
import java.awt.Graphics2D
import java.util.regex.Matcher

class Screenshot extends ScreenshotReporter {

    protected final Spec _specification
    protected final Browser _browser

    private String _label
    private ScreenImage _screenImage
    private ScreenImage _template

    Screenshot(Spec specification) {
        _specification = specification
        _browser = _specification.browser
        _screenImage = screendump()
    }

    protected ScreenImage screendump(Browser browser = _browser) {
        ScreenImage image
        TakesScreenshot screenshotDriver = determineScreenshotDriver(browser)
        if (screenshotDriver) {
            image = new ScreenImage(screenshotDriver.getScreenshotAs(OutputType.BYTES))
        }
        image
    }

    boolean validate() {
        if (_specification.learnMode) {
            return true
        }
        int distance = screenImage.distance(template)
        int limit = _browser.config.properties.get('maxPHasDistance', 3)
        _specification.report "validate ${screenshotName}: ${screenImage.hash} =~ ${template ? template.hash : 'no template'} ? distance: ${distance} <= limit: ${limit} ! ${distance <= limit ? 'ok' : 'fail'}"
        distance <= limit
    }

    ScreenImage getScreenImage() {
        _screenImage
    }

    ScreenImage getTemplate() {
        if (!_template) {
            File file = getFile(_specification.reportDir, getTemplateName(), 'png')
            if (file.canRead()) {
                _template = new ScreenImage(file)
                Reporting.instance.image(file, 'template', 'screen')
            }
        }
        _template
    }

    Screenshot save(String label = null) {
        _label = label
        String filename = _specification.learnMode ? getTemplateName() : getScreenshotName()
        File file = getFile(_specification.reportDir, filename, 'png')
        println "save image: ${file.absolutePath}"
        screenImage.save(file)
        Reporting.instance.image(file, 'screenshot', 'screen')
        this
    }

    String getScreenshotName(String label = _label) {
        getFileName(label, 'screenshot')
    }

    String getTemplateName(String label = _label) {
        getFileName(label, 'template')
    }

    protected String getFileName(String label = null, String type) {
        (label ? (label + '_' + type) : type).replace(' ', '_')
    }

    /**
     * Hide the area which the navigator references (fill area of each navigator element with white color).
     * Useful to remove dynamic content elements from the screenshot.
     */
    Screenshot hide(Navigator navigator) {
        if (navigator) {
            for (WebElement element : navigator.allElements()) {
                Graphics2D g = screenImage.image.createGraphics()
                g.setColor(Color.WHITE)
                g.fillRect(element.location.x - 1, element.location.y - 1, element.size.width + 2, element.size.height + 2)
                g.dispose()
            }
        }
        this
    }

    /**
     * Reduce the image to the area defined by the first element of the given navigator.
     */
    Screenshot crop(Navigator navigator) {
        if (navigator) {
            WebElement element = navigator.firstElement()
            if (element) {
                crop(element.location.x - 1, element.location.y - 1, element.size.width + 2, element.size.height + 2)
            }
        }
        this
    }

    Screenshot crop(String xPos = 'center', String yPos = '0', String width = 'max', String height = 'max') {
        int maxWidth = screenImage.image.getWidth()
        int maxHeight = screenImage.image.getHeight()
        int w = getImageValue(width, maxWidth);
        int h = getImageValue(height, maxHeight);
        int x = getImageValue(xPos, maxWidth, w);
        int y = getImageValue(yPos, maxHeight, h);
        crop(x, y, w, h)
    }

    Screenshot crop(int x, int y, int w, int h) {
        int maxWidth = screenImage.image.getWidth()
        int maxHeight = screenImage.image.getHeight()
        if (x < 0) {
            x = 0; w--
        }
        if (y < 0) {
            y =0; h--
        }
        if (x + w > maxWidth) w = maxWidth - x;
        if (y + h > maxHeight) h = maxHeight - y;
        screenImage.setImage(screenImage.image.getSubimage(x, y, w, h))
        this
    }

    protected int getImageValue(String rule, int maxValue, int related = 0) {
        int value
        switch (rule) {
            case null:
            case 'max': value = maxValue; break
            case 'center': value = (maxValue - related) / 2; break
            case ~/(\d+)%/: value = maxValue * Integer.parseInt(Matcher.lastMatcher[0][1]) / 100; break
            default: value = rule as int
        }
        if (value > maxValue) value = maxValue
        if (value < 0) value = 0
        value
    }

    String toString() {
        _screenImage.toString()
    }
}
