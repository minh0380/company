package kr.co.company.vacation;

import org.springframework.stereotype.Service;

@Service
public class VacationService {
	
	private final VacationRepository vacationRepository;
	
	public VacationService(VacationRepository vacationRepository) {
		this.vacationRepository = vacationRepository;
	}
	
	public void saveVacation(Vacation vacation) {
		vacationRepository.save(vacation);
	}

}
