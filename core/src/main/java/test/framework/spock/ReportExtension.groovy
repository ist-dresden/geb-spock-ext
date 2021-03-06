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
                reporting.description _spec.description?.toString()
            }

            @Override
            void beforeFeature(FeatureInfo feature) {
                Reporting reporting = Reporting.instance
                reporting.adjustSection(feature.parent.reflection, feature.name)
                reporting.description feature.description?.toString()
            }

            @Override
            void beforeIteration(IterationInfo iteration) {
                Reporting reporting = Reporting.instance
                Object[] dataValues = iteration.dataValues
                if (dataValues && dataValues.length > 0) {
                    reporting.indent()
                    reporting.write "<h5 class=\"loop-data\">"
                    for (Object value : dataValues) {
                        reporting.write "<span class=\"value\">${value}</span>"
                    }
                    reporting.write "</h5>"
                }
                Reporting.Result result = reporting.getResult()
                result.running = true
            }

            @Override
            void afterIteration(IterationInfo iteration) {
                Reporting reporting = Reporting.instance
                reporting.output()
                Reporting.Result result = reporting.getResult()
                if (result.running) {
                    reporting.success()
                }
            }

            @Override
            void afterFeature(FeatureInfo feature) {
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
