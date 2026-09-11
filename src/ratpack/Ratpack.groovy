import asset.pipeline.ratpack.AssetPipelineModule
import com.sheehan.jobdsl.CustomSecurityManager
import com.sheehan.jobdsl.ScriptExecutionModule
import com.sheehan.jobdsl.ScriptExecutor
import ratpack.form.Form
import ratpack.groovy.template.TextTemplateModule

import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.groovy.Groovy.ratpack

// SecurityManager was deprecated in Java 17 and cannot be installed from Java 24 onward.
if (CustomSecurityManager.legacySandboxAvailable()) {
    try {
        System.securityManager = new CustomSecurityManager()
    } catch (UnsupportedOperationException ignored) {
        // Script execution fails closed if the legacy sandbox cannot be installed.
    }
}

ratpack {

	bindings {
		module ScriptExecutionModule
		module TextTemplateModule, { TextTemplateModule.Config config -> config.staticallyCompile = true }

		module(AssetPipelineModule) { config ->
			config.sourcePath '../../../src/assets'
		}
	}

	handlers {
        get {
            render groovyTemplate('index.html')
        }

		post('execute') { ScriptExecutor scriptExecutor ->
			parse(Form).then { Form form ->
				String script = form.script
				render scriptExecutor.execute(script)
			}
		}
	}
}


