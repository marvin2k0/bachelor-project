package org.example.backend.allocation;

import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.config.solver.SolverConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class OptaPlannerConfig {
    @Bean
    public SolverConfig solverConfig() {
        return new SolverConfig()
                .withSolutionClass(AllocationSolution.class)
                .withEntityClasses(GroupAssignment.class)
                .withConstraintProviderClass(AllocationConstraintProvider.class)
                .withTerminationSpentLimit(Duration.ofSeconds(10));
    }

    @Bean
    public SolverFactory<AllocationSolution> solverFactory(SolverConfig solverConfig) {
        return SolverFactory.create(solverConfig);
    }

    @Bean
    public SolverManager<AllocationSolution, Long> solverManager(
            SolverFactory<AllocationSolution> solverFactory) {
        return SolverManager.create(solverFactory);
    }
}
