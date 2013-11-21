package test.framework.spock

import org.spockframework.runtime.IRunListener
import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.model.ErrorInfo
import org.spockframework.runtime.model.FeatureInfo
import org.spockframework.runtime.model.IterationInfo
import org.spockframework.runtime.model.SpecInfo
import test.framework.report.Reporting

class ReportExtension implements IGlobalExtension {

    @Override
    void visitSpec(SpecInfo spec) {

        spec.addListener(new IRunListener() {

            @Override
            void beforeSpec(SpecInfo _spec) {
                Reporting reporting = Reporting.instance
                reporting.adjustSection(spec.reflection)
                reporting.indent()
                reporting.info "<div class=\"description\">"
                reporting.info _spec.description?.toString()
                reporting.indent()
                reporting.info "</div>"
            }

            @Override
            void beforeFeature(FeatureInfo feature) {
                Reporting reporting = Reporting.instance
                reporting.adjustSection(feature.parent.reflection, feature.name)
                reporting.reset()
                reporting.indent()
                reporting.info "<div class=\"description\">"
                reporting.info feature.description?.toString()
                reporting.indent()
                reporting.info "</div>"
            }

            @Override
            void beforeIteration(IterationInfo iteration) {
                //Reporting.instance.info "beforeIteration(${iteration.name})..."
            }

            @Override
            void afterIteration(IterationInfo iteration) {
                //Reporting.instance.info "afterIteration(${iteration.name})..."
            }

            @Override
            void afterFeature(FeatureInfo feature) {
                Reporting reporting = Reporting.instance
                reporting.indent(); reporting.info "<div class=\"output\">"
                reporting.text(Reporting.instance.sniffed)
                reporting.indent(); reporting.info "</div>"
                Reporting.Result result = reporting.getResult()
                if (result._successCount < 1 && result._failureCount == 0) {
                    reporting.success()
                }
            }

            @Override
            void afterSpec(SpecInfo _spec) {
                Reporting reporting = Reporting.instance
                reporting.adjustSection(_spec.reflection)
            }

            @Override
            void error(ErrorInfo error) {
                Reporting.instance.error error
            }

            @Override
            void specSkipped(SpecInfo _spec) {
                Reporting.instance.info "specSkipped(${_spec})..."
            }

            @Override
            void featureSkipped(FeatureInfo feature) {
                Reporting.instance.info "featureSkipped(${feature})..."
            }
        })
    }
}
