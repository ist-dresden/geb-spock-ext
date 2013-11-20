package test.framework.spock

import org.spockframework.runtime.IRunListener
import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.extension.IMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.ErrorInfo
import org.spockframework.runtime.model.FeatureInfo
import org.spockframework.runtime.model.IterationInfo
import org.spockframework.runtime.model.SpecInfo
import test.framework.io.TeePrintBuffer
import test.framework.report.Reporting

class ReportExtension implements IGlobalExtension {

    @Override
    void visitSpec(SpecInfo spec) {

        //Reporting.instance.adjustSection(spec.reflection)
        //Reporting.instance.info "visitSpec(${spec})..."
        //Reporting.instance.info "spec.description: ${spec.description}"

        /*
        spec.addInterceptor(new IMethodInterceptor() {
            @Override
            void intercept(IMethodInvocation invocation) throws Throwable {
                println "method.intercept(${invocation.method})..."
                invocation.proceed()
            }
        })

        println "interceptors: ${spec.getInterceptors()}"
        */

        spec.addListener(new IRunListener() {

            @Override
            void beforeSpec(SpecInfo _spec) {
                Reporting.instance.adjustSection(spec.reflection)
                Reporting.instance.indent()
                Reporting.instance.info "<div class=\"description\">"
                Reporting.instance.info _spec.description?.toString()
                Reporting.instance.indent()
                Reporting.instance.info "</div>"
            }

            @Override
            void beforeFeature(FeatureInfo feature) {
                Reporting.instance.adjustSection(feature.parent.reflection, feature.name)
                Reporting.instance.reset()
                Reporting.instance.indent()
                Reporting.instance.info "<div class=\"description\">"
                Reporting.instance.info feature.description?.toString()
                Reporting.instance.indent()
                Reporting.instance.info "</div>"
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
