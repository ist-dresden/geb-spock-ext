environments {

    general {
        suite = {
            [
                    'TestSuite'
            ]
        }
    }

    report {
        suite = {
            [
                    'test.example.report.ReportSuite'
            ]
        }
    }

    examples {
        suite = {
            [
                    'test.example.ExamplesSuite'
            ]
        }
    }

    mylist {
        suite = {
            [
                    'test.example.report.ReportTest',
                    'test.example.report.subsection.ReportTest3'
            ]
        }
    }

    cq {
        suite = {
            [
                    'test.example.CQExample'
            ]
        }
    }
}